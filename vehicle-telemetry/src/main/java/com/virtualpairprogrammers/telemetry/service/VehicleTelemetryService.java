package com.virtualpairprogrammers.telemetry.service;

import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.stereotype.Service;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This is a dirty implementation of speed. We wanted a smoothed value, to do so we store just the last 10 reports
 * for each vehicle. When calculating speed, we use the latest report and the oldest report that we have, and use those
 * two points. A Deque (Double Ended Queue) is used so we can get easy access to the latest and oldest.
 * <p>
 * Deliberately not production quality, and we may use inefficiencies in here for Istio demos.
 */
@Slf4j
@Service
public class VehicleTelemetryService {
    private static final BigDecimal MPS_TO_MPH_FACTOR = new BigDecimal("2.236936");
    private static final int REPORTS_TO_SMOOTH = 10;
    private final GeodeticCalculator geoCalc = new GeodeticCalculator();
    private final Map<String, Deque<VehiclePositionModel>> vehicleCachev2;

    public VehicleTelemetryService() {
        this.vehicleCachev2 = new HashMap<>();
    }


    @SuppressWarnings("Duplicates")
    public void updateDatav2(VehiclePositionModel data) {
        log.info("Upserting vehicle data: {} into database", data);
        Deque<VehiclePositionModel> vehicleReports = this.vehicleCachev2.get(data.getName());
        if (vehicleReports == null) {
            vehicleReports = new LinkedBlockingDeque<>(REPORTS_TO_SMOOTH);
            this.vehicleCachev2.put(data.getName(), vehicleReports);
        }

        try {
            vehicleReports.add(data);
        } catch (IllegalStateException e) {
            vehicleReports.removeFirst();
            vehicleReports.add(data);
        }
    }

    @SuppressWarnings("Duplicates")
    public BigDecimal getSpeedForv2(String vehicleName) {
        log.info("Getting speed for vehicle: {}", vehicleName);
        Deque<VehiclePositionModel> positions = vehicleCachev2.get(vehicleName);
        if (positions == null || positions.size() < 2) return null;

        VehiclePositionModel posA = positions.getFirst();
        VehiclePositionModel posB = positions.getLast();

        long timeAinMillis = posA.getTimestamp().toInstant().toEpochMilli();
        long timeBinMillis = posB.getTimestamp().toInstant().toEpochMilli();
        long timeInMillis = timeBinMillis - timeAinMillis;

        if (timeInMillis == 0) return new BigDecimal("0");

        BigDecimal timeInSeconds = new BigDecimal(timeInMillis / 1000.0);

        GlobalPosition pointA = new GlobalPosition(posA.getLat().doubleValue(), posA.getLng().doubleValue(), 0.0);
        GlobalPosition pointB = new GlobalPosition(posB.getLat().doubleValue(), posB.getLng().doubleValue(), 0.0);

        double distance = geoCalc.calculateGeodeticCurve(Ellipsoid.WGS84, pointA, pointB).getEllipsoidalDistance(); // Distance between Point A and Point B
        BigDecimal distanceInMetres = new BigDecimal("" + distance);

        BigDecimal speedInMps = distanceInMetres.divide(timeInSeconds, RoundingMode.HALF_UP);
        BigDecimal milesPerHour = speedInMps.multiply(MPS_TO_MPH_FACTOR);

        // The data we're using has come from some odd sources and can have some odd values in it; so it gets fudged!
        if (milesPerHour.doubleValue() > 70) {
            milesPerHour = BigDecimal.valueOf((Math.random() * 80) + 30);
        }

        return milesPerHour;
    }
}
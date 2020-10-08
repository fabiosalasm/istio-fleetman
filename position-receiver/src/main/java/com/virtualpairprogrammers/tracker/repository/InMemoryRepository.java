package com.virtualpairprogrammers.tracker.repository;

import com.virtualpairprogrammers.tracker.domain.VehicleNotFoundException;
import com.virtualpairprogrammers.tracker.externalservices.StaffClient;
import com.virtualpairprogrammers.tracker.externalservices.TelemetryServiceUnavailableException;
import com.virtualpairprogrammers.tracker.externalservices.VehicleTelemetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * This is a quick and dirty dev-standin (for local testing) that stores vehicle position reports
 * in a memory structure.
 * <p>
 * Thread safety is NOT OFFERED; remember this is intended to be used in local development.
 * <p>
 * Use the database backed implementation in a production, clustered or cloud environment.
 */
@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Repository
public class InMemoryRepository implements Repository {
    private final VehicleTelemetryService telemetryService;
    // See case #22, bad code but we were forced to do this!
    private final StaffClient staffClient;
    private Map<String, TreeSet<VehiclePositionModel>> positionDatabase = new HashMap<>();

    @Override
    public void updatePosition(VehiclePositionModel data) {
        String vehicleName = data.getName();
        TreeSet<VehiclePositionModel> positions = positionDatabase.computeIfAbsent(vehicleName, k -> new TreeSet<>());
        var vehicleBuilder = VehiclePositionModel.builder()
                .name(data.getName())
                .lat(data.getLat())
                .lng(data.getLng())
                .timestamp(OffsetDateTime.now());

        try {
            BigDecimal calculatedSpeed = Optional.ofNullable(telemetryService.getSpeedFor(vehicleName))
                    .orElse(BigDecimal.ZERO);

            vehicleBuilder.speed(calculatedSpeed);

        } catch (TelemetryServiceUnavailableException e) {
            log.warn("There was an error when calculating speed for vehicle: {} " +
                    "in the upstream service. Setting speed to 0", vehicleName);
            vehicleBuilder.speed(BigDecimal.ZERO);
        }

        var vehicle = vehicleBuilder.build();
        positions.add(vehicle);
        telemetryService.updateData(data); // see case #8 for details on why we do this last
        staffClient.updateSpeedDataFor(vehicle.getName(), vehicle.getSpeed().toString());
    }

    @Override
    public VehiclePositionModel getLatestPositionFor(String vehicleName) throws VehicleNotFoundException {
        TreeSet<VehiclePositionModel> reportsForThisVehicle = positionDatabase.get(vehicleName);
        if (reportsForThisVehicle == null) throw new VehicleNotFoundException();
        return reportsForThisVehicle.first();
    }

    @Override
    public void addAllReports(VehiclePositionModel[] allReports) {
        for (VehiclePositionModel next : allReports) {
            this.updatePosition(next);
        }
    }

    @Override
    public TreeSet<VehiclePositionModel> getAllReportsForVehicleSince(String name, Date timestamp)
            throws VehicleNotFoundException {

        if (timestamp == null) timestamp = new java.util.Date(1);

        // Could use a Java 8 lambda to filter the collection but I'm playing safe in targeting Java 7
        TreeSet<VehiclePositionModel> vehicleReports = this.positionDatabase.get(name);
        if (vehicleReports == null) throw new VehicleNotFoundException();

        VehiclePositionModel example = VehiclePositionModel.builder()
                .name(name)
                .timestamp(timestamp.toInstant().atOffset(ZoneOffset.UTC))
                .build();
        return (TreeSet<VehiclePositionModel>) (vehicleReports.headSet(example, true));
    }

    @Override
    public Set<VehiclePositionModel> getLatestPositionsOfAllVehiclesUpdatedSince(Date since) {
        Set<VehiclePositionModel> results = new HashSet<>();

        for (String vehicleName : this.positionDatabase.keySet()) {
            TreeSet<VehiclePositionModel> reports;
            try {
                reports = this.getAllReportsForVehicleSince(vehicleName, since);
                if (!reports.isEmpty()) results.add(reports.first());
            } catch (VehicleNotFoundException e) {
                // Can't happen as we know the vehicle exists
                assert false;
            }
        }
        return results;
    }

    public Collection<VehiclePositionModel> getHistoryFor(String vehicleName) throws VehicleNotFoundException {
        return this.getAllReportsForVehicleSince(vehicleName, new Date(0L));
    }

    public void reset() {
        positionDatabase.clear();
    }
}

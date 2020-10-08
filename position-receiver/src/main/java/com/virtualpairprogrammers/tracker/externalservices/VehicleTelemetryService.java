package com.virtualpairprogrammers.tracker.externalservices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.math.BigDecimal;

/**
 * This external service is capable of providing telemetry for Vehicles.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleTelemetryService {
    private final VehicleTelemetryClient client;

    public void updateData(VehiclePositionModel vehicleName) {
        try {
            client.updateData(vehicleName);
        } catch (Exception e) {
            log.info("Telemetry service unavailable. Unable to update data for " + vehicleName);
        }
    }

    public BigDecimal getSpeedFor(String vehicleName) throws TelemetryServiceUnavailableException {
        try {
            return client.getSpeedFor(vehicleName);
        } catch (Exception e) {
            log.info("Telemetry service unavailable. Cannot get speed for vehicle " + vehicleName);
            throw new TelemetryServiceUnavailableException();
        }
    }
}
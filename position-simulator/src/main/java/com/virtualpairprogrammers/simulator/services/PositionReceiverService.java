package com.virtualpairprogrammers.simulator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

@Slf4j
@RequiredArgsConstructor
@Service
public class PositionReceiverService {
    private final PositionReceiverClient positionReceiverClient;

    public void sendReportToPositionTracker(VehiclePositionModel report) {
        positionReceiverClient.sendNewPositionReport(report);
    }

    public void clearHistories() {
        try {
            positionReceiverClient.clearHistories();
        } catch (Exception e) {
            log.warn("Failed to clear history for vehciles.");
        }
    }
}


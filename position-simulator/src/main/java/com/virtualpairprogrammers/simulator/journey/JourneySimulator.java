package com.virtualpairprogrammers.simulator.journey;

import com.virtualpairprogrammers.simulator.PositionsimulatorApplication;
import com.virtualpairprogrammers.simulator.services.PositionReceiverService;
import com.virtualpairprogrammers.simulator.utils.VehicleNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Invoked periodically by Quartz, a random position update is selected and transmitted.
 */
@RequiredArgsConstructor
@Component
public class JourneySimulator {
    private final PositionReceiverService positionReceiverService;
    private Map<String, Queue<String>> reports = new HashMap<>();
    private List<String> vehicleNames = new ArrayList<>();

    /**
     * Read the data from the resources directory - should work for an executable Jar as
     * well as through direct execution
     */
    @PostConstruct
    private void setUpData() {
        PathMatchingResourcePatternResolver path = new PathMatchingResourcePatternResolver();
        try {
            for (Resource nextFile : path.getResources("tracks/*")) {
                URL resource = nextFile.getURL();
                File f = new File(resource.getFile());
                String vehicleName = VehicleNameUtils.prettifyName(f.getName());
                vehicleNames.add(vehicleName);
                populateReportQueueForVehicle(vehicleName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        positionReceiverService.clearHistories();
    }

    private void populateReportQueueForVehicle(String vehicleName) {
        InputStream is = PositionsimulatorApplication.class.getResourceAsStream("/tracks/" + VehicleNameUtils.uglifyName(vehicleName));
        try (Scanner sc = new Scanner(is)) {
            Queue<String> thisVehicleReports = new LinkedBlockingQueue<>();
            while (sc.hasNextLine()) {
                String nextReport = sc.nextLine();
                thisVehicleReports.add(nextReport);
            }
            reports.put(vehicleName, thisVehicleReports);
        }
    }


    @Scheduled(fixedDelay = 100)
    public void randomPositionUpdate() {
        // Random jitter. Sometimes we'll do nothing
        if (Math.random() < 0.9) return;

        // Choose random vehicle
        int position = (int) (Math.random() * vehicleNames.size());
        String chosenVehicleName = vehicleNames.get(position);

        // Grab next report for this vehicle
        // To smooth out fluctuations, we'll miss out lots of reports
        int randomReportDrop = (int) (Math.random() * 10);
        String nextReport = null;
        for (int i = 0; i <= randomReportDrop; i++) {
            nextReport = reports.get(chosenVehicleName).poll();
            if (nextReport == null) {
                System.out.println("Journey over for " + chosenVehicleName + ". Restarting route");
                populateReportQueueForVehicle(chosenVehicleName);
                nextReport = reports.get(chosenVehicleName).poll();
            }
        }

        var report = getVehicleDataFromRawString(chosenVehicleName, nextReport);
        positionReceiverService.sendReportToPositionTracker(report);
    }

    private VehiclePositionModel getVehicleDataFromRawString(String chosenVehicleName, String nextReport) {
        String[] data = nextReport.split("\"");
        String lat = data[1];
        String lng = data[3];

        return VehiclePositionModel.builder().
                name(chosenVehicleName)
                .lat(new BigDecimal(lat))
                .lng(new BigDecimal(lng))
                .timestamp(OffsetDateTime.now())
                .build();
    }
}

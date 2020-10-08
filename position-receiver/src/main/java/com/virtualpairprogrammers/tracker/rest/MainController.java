package com.virtualpairprogrammers.tracker.rest;

import com.virtualpairprogrammers.tracker.domain.VehicleNotFoundException;
import com.virtualpairprogrammers.tracker.repository.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MainController {
    private final Repository repository;

    @RequestMapping(method = RequestMethod.POST, value = "/vehicles")
    public void receiveUpdatedPostion(@RequestBody VehiclePositionModel newReport) {
        repository.updatePosition(newReport);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vehicles/{vehicleName}")
    public VehiclePositionModel getLatestReportForVehicle(@PathVariable String vehicleName) throws VehicleNotFoundException {
        return repository.getLatestPositionFor(vehicleName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/history/{vehicleName}")
    public Collection<VehiclePositionModel> getEntireHistoryForVehicle(@PathVariable String vehicleName,
                                                                       HttpServletRequest request) throws VehicleNotFoundException {
        // Peek at request headers...
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getHeader(name);
            log.info("Got the header: {}, {}", name, value);
        }

        return repository.getHistoryFor(vehicleName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vehicles")
    public Collection<VehiclePositionModel> getUpdatedPositions(@RequestParam(value = "since", required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date since) {
        return repository.getLatestPositionsOfAllVehiclesUpdatedSince(since);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/vehicles")
    public void resetHistories() {
        repository.reset();
    }
}

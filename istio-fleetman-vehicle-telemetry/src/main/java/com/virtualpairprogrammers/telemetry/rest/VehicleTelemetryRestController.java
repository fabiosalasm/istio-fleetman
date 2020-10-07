package com.virtualpairprogrammers.telemetry.rest;

import com.virtualpairprogrammers.telemetry.service.VehiclePosition;
import com.virtualpairprogrammers.telemetry.service.VehicleTelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class VehicleTelemetryRestController {
    private final VehicleTelemetryService service;

    @RequestMapping(method = RequestMethod.POST, value = "/vehicles")
    public void updateData(@RequestBody VehiclePosition data) {
        this.service.updateData(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vehicles/{vehicleName}")
    public BigDecimal getSpeedFor(@PathVariable("vehicleName") String vehicleName) {
        return this.service.getSpeedFor(vehicleName);
    }
}

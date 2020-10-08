package com.virtualpairprogrammers.telemetry.rest;

import com.virtualpairprogrammers.telemetry.service.VehicleTelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class VehicleTelemetryRestController {
    private final VehicleTelemetryService service;

    @PostMapping(value = "/v2/vehicles", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(code = HttpStatus.OK)
    public void updateDatav2(@RequestBody VehiclePositionModel data) {
        service.updateDatav2(data);
    }

    @GetMapping(value = "/v2/vehicles/{vehicleName}")
    public BigDecimal getSpeedForv2(@PathVariable String vehicleName) {
        return service.getSpeedForv2(vehicleName);
    }
}

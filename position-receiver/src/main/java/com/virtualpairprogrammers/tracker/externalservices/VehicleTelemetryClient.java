package com.virtualpairprogrammers.tracker.externalservices;

import com.virtualpairprogrammers.tracker.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.math.BigDecimal;

@FeignClient(name = "vehicle-telemetry", url = "${vehicle-telemetry.url}", configuration = FeignConfig.class)
public interface VehicleTelemetryClient {
    @RequestMapping(method = RequestMethod.POST, value = "/v2/vehicles")
    void updateData(VehiclePositionModel data);

    @RequestMapping(method = RequestMethod.GET, value = "/v2/vehicles/{vehicleName}")
    BigDecimal getSpeedFor(@PathVariable("vehicleName") String vehicleName);
}

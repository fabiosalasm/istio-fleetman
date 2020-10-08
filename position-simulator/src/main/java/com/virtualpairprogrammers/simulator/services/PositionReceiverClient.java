package com.virtualpairprogrammers.simulator.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

@FeignClient(name = "position-receiver-service", url = "${position-receiver-service.url}")
public interface PositionReceiverClient {
    @RequestMapping(method = RequestMethod.POST, value = "/vehicles")
    void sendNewPositionReport(@RequestBody VehiclePositionModel report);

    @RequestMapping(method = RequestMethod.DELETE, value = "/vehicles")
    void clearHistories();
}

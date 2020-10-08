package com.virtualpairprogrammers.staffmanagement.service;

import com.virtualpairprogrammers.staffmanagement.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by fabio.salas (fabio.salas@globant.com) on 7/10/2020
 **/
@FeignClient(name = "driver-monitoring-service", url = "${driver-monitoring-service.url}", configuration = FeignConfig.class)
public interface ExternalServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/driver")
    void updateSpeedLogFor(@RequestParam("name") String name, @RequestBody String speed);
}
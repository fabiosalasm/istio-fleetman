package com.virtualpairprogrammers.tracker.externalservices;

import com.virtualpairprogrammers.tracker.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "staff-management-service", url = "${staff-management-service.url}", configuration = FeignConfig.class)
public interface StaffClient {
    @RequestMapping(method = RequestMethod.POST, value = "/driver/{vehicleName}/{speed}")
    void updateSpeedDataFor(@PathVariable("vehicleName") String vehicleName, @PathVariable("speed") String speed);
}

package com.virtualpairprogrammers.staffmanagement.rest;

import com.virtualpairprogrammers.staffmanagement.domain.StaffRecord;
import com.virtualpairprogrammers.staffmanagement.service.ExternalService;
import com.virtualpairprogrammers.staffmanagement.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StaffManagementController {
    private final StaffService staffService;
    private final ExternalService externalService;

    @GetMapping(value = "/driver/{vehicleName}")
    public StaffRecord getDriverAssignedTo(@PathVariable String vehicleName) {
        return staffService.getDriverDetailsFor(vehicleName);
    }

    @PostMapping(value = "/driver/{vehicleName}/{speed}")
    public void updateSpeedLogFor(@PathVariable String vehicleName, @PathVariable String speed) {
        StaffRecord driverDetails = staffService.getDriverDetailsFor(vehicleName);
        externalService.updateSpeedLogFor(driverDetails.getName(), speed);
    }
}

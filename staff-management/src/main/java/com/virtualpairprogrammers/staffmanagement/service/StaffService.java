package com.virtualpairprogrammers.staffmanagement.service;

import com.virtualpairprogrammers.staffmanagement.domain.StaffRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class StaffService {
    private static final String PLACEHOLDER = "https://avatars0.githubusercontent.com/u/16841947?s=460&v=4";
    private final Map<String, String> drivers = Map.of("City Truck", "Pam Parry",
            "Huddersfield Truck A", "Duke T. Dog",
            "Huddersfield Truck B", "Denzil Tulser",
            "London Riverside", "Herman Boyce",
            "Village Truck", "June Snell");

    public StaffRecord getDriverDetailsFor(String vehicleName) {
        log.info("Getting the driver that is using the vehicle '{}'", vehicleName);
        return new StaffRecord(drivers.get(vehicleName), PLACEHOLDER);
    }
}

package com.virtualpairprogrammers.tracker;

import com.virtualpairprogrammers.tracker.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FeignConfig.class}))
public class PositionTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PositionTrackerApplication.class, args);
    }
}

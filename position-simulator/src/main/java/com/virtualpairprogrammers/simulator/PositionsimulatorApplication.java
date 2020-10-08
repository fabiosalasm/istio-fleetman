package com.virtualpairprogrammers.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Written for the Microservices course, this is a toy application which simulates the progress
 * of vehicles on a delivery route. The program reads from one or more text files containing
 * a list of lat/long positions (these can be created from .gpx files or similar).
 * <p>
 * Intended for use on the training videos, questions to contact@virtualpairprogrammers.com
 *
 * @author Richard Chesterwood
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class PositionsimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PositionsimulatorApplication.class);
    }

}


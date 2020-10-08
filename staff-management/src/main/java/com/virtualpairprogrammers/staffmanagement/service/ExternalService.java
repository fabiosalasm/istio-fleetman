package com.virtualpairprogrammers.staffmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalService {
    private final ExternalServiceClient client;

    public void updateSpeedLogFor(String name, String speed) {
        try {
            client.updateSpeedLogFor(name, speed);
        } catch (Exception e) {
            log.warn("Legacy driver monitoring system unavailable", e);
        }
    }
}
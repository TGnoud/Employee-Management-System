package com.example.employee_management.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemStatusScheduler {
    private static final Logger logger = LoggerFactory.getLogger(SystemStatusScheduler.class);

    @Scheduled(fixedRate = 30000)
    public void logSystemRunning() {
        logger.info("System running");
    }
}

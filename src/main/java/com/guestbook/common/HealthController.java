package com.guestbook.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @GetMapping("/health")
    public Map<String, Object> health() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptime = Duration.ofMillis(uptimeMs).toString()
                .substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", serviceName);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("uptime", uptime);
        return response;
    }
}

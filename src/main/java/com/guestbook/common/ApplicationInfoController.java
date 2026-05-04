package com.guestbook.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/application")
public class ApplicationInfoController {

    @Value("${service.name:${spring.application.name:unknown}}")
    private String serviceName;

    @Value("${service.version:1.0.0}")
    private String serviceVersion;

    @Value("${git.commit:unknown}")
    private String gitCommit;

    @Value("${git.branch:unknown}")
    private String gitBranch;

    @Value("${build.time:unknown}")
    private String buildTime;

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new LinkedHashMap<>();

        Map<String, String> service = new LinkedHashMap<>();
        service.put("name", serviceName);
        service.put("version", serviceVersion);
        response.put("service", service);

        Map<String, String> git = new LinkedHashMap<>();
        git.put("commit", gitCommit);
        git.put("branch", gitBranch);
        response.put("git", git);

        Map<String, String> build = new LinkedHashMap<>();
        build.put("time", buildTime);
        build.put("javaVersion", System.getProperty("java.version"));
        build.put("javaVendor", System.getProperty("java.vendor"));
        response.put("build", build);

        Map<String, String> runtime = new LinkedHashMap<>();
        runtime.put("os", System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        runtime.put("availableProcessors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        runtime.put("maxMemoryMB", String.valueOf(Runtime.getRuntime().maxMemory() / (1024 * 1024)));
        response.put("runtime", runtime);

        return response;
    }
}

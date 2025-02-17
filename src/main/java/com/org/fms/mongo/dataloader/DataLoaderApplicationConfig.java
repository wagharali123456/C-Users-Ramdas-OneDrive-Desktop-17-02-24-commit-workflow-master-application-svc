package com.org.fms.mongo.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.Application;
import com.org.fms.mongo.repository.ApplicationRepository;

@Configuration
public class DataLoaderApplicationConfig implements CommandLineRunner, Ordered {

    private final ApplicationRepository applicationRepository;
    private final ObjectMapper objectMapper;

    public DataLoaderApplicationConfig(ApplicationRepository applicationRepository, ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return 1;  // Ensures it runs first
    }

    @Override
    public void run(String... args) {
        try {
            long existingRecords = applicationRepository.count();
            if (existingRecords == 0) {
                List<Application> applications = loadApplicationsFromJson();
                
                if (applications != null && !applications.isEmpty()) {
                    validateGraphIntegrity(applications); // Graph-based validation
                    applicationRepository.saveAll(applications);
                    System.out.println("✅ [APPLICATIONS] Successfully inserted into MongoDB at: " + Instant.now());
                } else {
                    System.out.println("⚠️ [APPLICATIONS] No valid data found in JSON.");
                }
            } else {
                System.out.println("⚠️ [APPLICATIONS] Data already exists. Skipping insertion.");
            }
        } catch (Exception e) {
            System.err.println("❌ [ERROR] DataLoader encountered an issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Application> loadApplicationsFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("applications.json");
            InputStream inputStream = resource.getInputStream();
            String jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonData, new TypeReference<List<Application>>() {});
        } catch (IOException e) {
            System.err.println("❌ [ERROR] Failed to read applications.json: " + e.getMessage());
            return null;
        }
    }

    private void validateGraphIntegrity(List<Application> applications) {
        Set<String> allWorkflows = new HashSet<>();
        Set<String> referencedWorkflows = new HashSet<>();

        // Collect all workflows
        for (Application app : applications) {
            if (app.getConnectedWorkflows() != null) {
                allWorkflows.addAll(app.getConnectedWorkflows());
                referencedWorkflows.addAll(app.getConnectedWorkflows());
            }
        }

        // Detect orphaned workflows
        allWorkflows.removeAll(referencedWorkflows);
        if (!allWorkflows.isEmpty()) {
            System.err.println("⚠️ [WARNING] The following workflows are not linked to any application: " + allWorkflows);
        }
    }
}

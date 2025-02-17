package com.org.fms.mongo.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.WorkflowStageMapping;
import com.org.fms.mongo.repository.WorkflowStageMappingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class DataLoaderWorkflowStageMappingConfig implements CommandLineRunner, Ordered {

    private final WorkflowStageMappingRepository repository;
    private final ObjectMapper objectMapper;

    public DataLoaderWorkflowStageMappingConfig(WorkflowStageMappingRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return 6; // Load after workflows
    }

    @Override
    public void run(String... args) {
        try {
            if (repository.count() > 0) {
                System.out.println("⚠️ [WORKFLOW STAGE MAPPING] Data already exists. Skipping insertion.");
                return;
            }

            System.out.println("✅ [WORKFLOW STAGE MAPPING] Loading data...");
            
            InputStream inputStream = new ClassPathResource("workflow-stage-mapping.json").getInputStream();
            String jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            List<WorkflowStageMapping> mappings = objectMapper.readValue(jsonData, new TypeReference<List<WorkflowStageMapping>>() {});

            if (!mappings.isEmpty()) {
                repository.saveAll(mappings);
                System.out.println("✅ [WORKFLOW STAGE MAPPING] Successfully inserted into MongoDB.");
            } else {
                System.out.println("⚠️ [WORKFLOW STAGE MAPPING] No data found.");
            }

        } catch (Exception e) {
            System.err.println("❌ [ERROR] Failed to load workflow-stage-mapping.json: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

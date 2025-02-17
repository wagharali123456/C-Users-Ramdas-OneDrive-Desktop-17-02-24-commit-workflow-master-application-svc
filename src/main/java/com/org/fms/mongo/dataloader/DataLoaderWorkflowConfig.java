package com.org.fms.mongo.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.Workflow;
import com.org.fms.mongo.repository.WorkflowRepository;

@Configuration
public class DataLoaderWorkflowConfig implements CommandLineRunner, Ordered {

    private final WorkflowRepository workflowRepository;
    private final ObjectMapper objectMapper;

    public DataLoaderWorkflowConfig(WorkflowRepository workflowRepository, ObjectMapper objectMapper) {
        this.workflowRepository = workflowRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return 2;  // Runs after applications
    }

    @Override
    public void run(String... args) {
        try {
            if (workflowRepository.count() == 0) {  
                List<Workflow> workflows = loadWorkflowsFromJson();
                
                if (workflows != null && !workflows.isEmpty()) {
                    validateUniqueWorkflows(workflows);  // 🔥 Validates duplicate workflowIds

                    workflows.forEach(workflow -> {
                        workflow.setCreatedAt(Instant.now());
                        workflow.setUpdatedAt(Instant.now());
                    });

                    workflowRepository.saveAll(workflows);
                    System.out.println("✅ [WORKFLOWS] Successfully inserted into MongoDB at: " + Instant.now());
                } else {
                    System.out.println("⚠️ [WORKFLOWS] No valid data found in JSON.");
                }
            } else {
                System.out.println("⚠️ [WORKFLOWS] Data already exists. Skipping insertion.");
            }
        } catch (Exception e) {
            System.err.println("❌ [ERROR] Workflow DataLoader encountered an issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Workflow> loadWorkflowsFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("workflows.json");
            InputStream inputStream = resource.getInputStream();
            String jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonData, new TypeReference<List<Workflow>>() {});
        } catch (IOException e) {
            System.err.println("❌ [ERROR] Failed to read workflows.json: " + e.getMessage());
            return null;
        }
    }

    private void validateUniqueWorkflows(List<Workflow> workflows) {
        Set<String> workflowIds = new HashSet<>();
        for (Workflow workflow : workflows) {
            if (!workflowIds.add(workflow.getWorkflowId())) {
                throw new IllegalStateException("❌ Duplicate workflowId detected: " + workflow.getWorkflowId());
            }
        }
    }
}

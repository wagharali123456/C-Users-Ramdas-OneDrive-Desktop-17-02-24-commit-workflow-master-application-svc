package com.org.fms.mongo.dataloader;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.GenericAction;
import com.org.fms.mongo.repository.GenericActionRepository;

@Configuration
@DependsOn({"dataLoaderApplicationConfig", "dataLoaderWorkflowConfig"})
public class DataLoaderActionConfig {

    @Bean
    public CommandLineRunner initActions(GenericActionRepository genericActionRepository) {
        return args -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                
                // ✅ Load Generic Actions from JSON
                if (genericActionRepository.count() == 0) {
                    List<GenericAction> genericActions = objectMapper.readValue(
                        new ClassPathResource("Action.json").getInputStream(),
                        new TypeReference<List<GenericAction>>() {}
                    );

                    genericActionRepository.saveAll(genericActions);
                    System.out.println("✅ [GENERIC ACTIONS] Successfully inserted into MongoDB at: " + Instant.now());
                } else {
                    System.out.println("⚠️ [GENERIC ACTIONS] Already exist. Skipping insertion.");
                }

//                // ✅ Load Workflow Stage Actions from JSON
//                if (actionRepository.count() == 0) {
//                    List<Action> workflowActions = objectMapper.readValue(
//                        new ClassPathResource("Action.json").getInputStream(),
//                        new TypeReference<List<Action>>() {}
//                    );
//
//                    actionRepository.saveAll(workflowActions);
//                    System.out.println("✅ [WORKFLOW ACTIONS] Successfully inserted into MongoDB at: " + Instant.now());
//                } else {
//                    System.out.println("⚠️ [WORKFLOW ACTIONS] Already exist. Skipping insertion.");
//                }
            } catch (Exception e) {
                System.err.println("❌ [ERROR] DataLoader encountered an issue: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}

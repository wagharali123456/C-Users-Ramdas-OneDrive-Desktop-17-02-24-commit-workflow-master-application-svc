package com.org.fms.mongo.dataloader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.Stage;
import com.org.fms.mongo.repository.StageRepository;

@Configuration
public class DataLoaderStage implements CommandLineRunner, Ordered {

    private final StageRepository stageRepository;
    private final ObjectMapper objectMapper;

    public DataLoaderStage(StageRepository stageRepository, ObjectMapper objectMapper) {
        this.stageRepository = stageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return 4; // Runs after WorkflowConfig
    }

    @Override
    public void run(String... args) {
        try {
            if (stageRepository.count() > 0) {
                System.out.println("⚠️ [STAGES] Data already exists. Skipping insertion.");
                return;
            }

            System.out.println("✅ [STAGES] Loading stage data...");

            // ✅ Load JSON file for stage definitions
            List<Stage> stages = loadStagesFromJson();
            if (stages == null || stages.isEmpty()) {
                System.err.println("❌ [ERROR] No valid stages found in JSON.");
                return;
            }

            // ✅ Handle fallback timeout stage relationships using graph traversal
            Map<Integer, Stage> stageMap = new HashMap<>();
            for (Stage stage : stages) {
                stage.setCreatedAt(Instant.now());
                stage.setUpdatedAt(Instant.now());
                stageMap.put(stage.getStageId(), stage);
            }

            // ✅ Traverse graph & validate fallback stages
            for (Stage stage : stages) {
                if (stage.getTimeoutConfig() != null && stage.getTimeoutConfig().isTimeoutEnabled()) {
                    Integer fallbackStageId = stage.getTimeoutConfig().getFallbackStageId();

                    if (fallbackStageId != null) {
                        if (!stageMap.containsKey(fallbackStageId)) {
                            System.err.println("⚠️ [WARNING] Fallback Stage ID " + fallbackStageId + " for " + stage.getName() + " does not exist.");
                        } else {
                            System.out.println("✅ Validated fallback stage for " + stage.getName() + " -> " + fallbackStageId);
                        }
                    }
                }
            }

            // ✅ Insert validated stages into MongoDB
            stageRepository.saveAll(stages);
            System.out.println("✅ [STAGES] Successfully inserted " + stages.size() + " stages into MongoDB at " + Instant.now());

        } catch (Exception e) {
            System.err.println("❌ [ERROR] DataLoaderStage encountered an issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Stage> loadStagesFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("stages.json").getInputStream();
            String jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonData, new TypeReference<List<Stage>>() {});
        } catch (Exception e) {
            System.err.println("❌ [ERROR] Failed to read stages.json: " + e.getMessage());
            return null;
        }
    }
}

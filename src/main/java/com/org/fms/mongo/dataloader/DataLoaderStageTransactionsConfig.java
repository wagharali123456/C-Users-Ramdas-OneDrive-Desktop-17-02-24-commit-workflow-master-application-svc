package com.org.fms.mongo.dataloader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.fms.mongo.model.StageTransaction;
import com.org.fms.mongo.repository.StageTransactionRepository;

@Configuration
public class DataLoaderStageTransactionsConfig implements CommandLineRunner, Ordered {

    private final StageTransactionRepository repository;
    private final ObjectMapper objectMapper;

    public DataLoaderStageTransactionsConfig(StageTransactionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public void run(String... args) {
        try {
            if (repository.count() > 0) {
                System.out.println("⚠️ [STAGE TRANSACTIONS] Data already exists. Skipping.");
                return;
            }

            InputStream inputStream = new ClassPathResource("stage-transcation.json").getInputStream();
            String jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            List<StageTransaction> stageTransactions = objectMapper.readValue(jsonData, new TypeReference<List<StageTransaction>>() {});

            repository.saveAll(stageTransactions);
            System.out.println("✅ [STAGE TRANSACTIONS] Inserted " + stageTransactions.size() + " transactions.");
        } catch (Exception e) {
            System.err.println("❌ [ERROR] DataLoader encountered an issue: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

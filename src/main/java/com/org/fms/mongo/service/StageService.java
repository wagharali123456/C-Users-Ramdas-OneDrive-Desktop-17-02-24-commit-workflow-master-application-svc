/*package com.org.fms.mongo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.org.fms.mongo.model.Stage;
import com.org.fms.mongo.repository.StageRepository;

@Service
public class StageService {
    private final StageRepository stageRepository;

    public StageService(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    // Get next stages (Forward traversal)
    public List<String> getNextStages(String stageId) {
        return stageRepository.findById(stageId)
                .map(Stage::getNextStages)
                .orElse(null);
    }

    // Get previous stages (Backward traversal)
    public List<String> getPreviousStages(String stageId) {
        return stageRepository.findById(stageId)
                .map(Stage::getPreviousStages)
                .orElse(null);
    }

    // Get rollback stages
    public List<String> getRollbackStages(String stageId) {
        return stageRepository.findById(stageId)
                .map(Stage::getRollbackStages)
                .orElse(null);
    }

    // Get skip stages
    public List<String> getSkipStages(String stageId) {
        return stageRepository.findById(stageId)
                .map(Stage::getSkipStages)
                .orElse(null);
    }


}
*/
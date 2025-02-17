/*package com.org.fms.mongo.service;

import org.springframework.stereotype.Service;
import com.org.fms.mongo.model.Action;
import com.org.fms.mongo.repository.ActionRepository;

import java.util.List;

@Service
public class ActionService {

    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<String> getAllowedActions(String appId, String workflowId, String stageId) {
        Action action = actionRepository.findByStageId(stageId).stream().findFirst().orElse(null);
        return (action != null) ? action.getAllowedActions() : null;
    }

    public List<String> getNotAllowedActions(String appId, String workflowId, String stageId) {
        Action action = actionRepository.findByStageId(stageId).stream().findFirst().orElse(null);
        return (action != null) ? action.getNotAllowedActions() : null;
    }
} */

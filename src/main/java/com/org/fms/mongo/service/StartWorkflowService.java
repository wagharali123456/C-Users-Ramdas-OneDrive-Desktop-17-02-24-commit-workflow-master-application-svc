package com.org.fms.mongo.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.org.fms.mongo.model.StageTransaction;
import com.org.fms.mongo.model.StartWorkflowRequest;
import com.org.fms.mongo.model.Workflow;
import com.org.fms.mongo.model.WorkflowStageMapping;
import com.org.fms.mongo.model.WorkflowTransaction;
import com.org.fms.mongo.model.WorkflowTransactionHistory;
import com.org.fms.mongo.repository.StageTransactionRepository;
import com.org.fms.mongo.repository.WorkflowRepository;
import com.org.fms.mongo.repository.WorkflowStageMappingRepository;
import com.org.fms.mongo.repository.WorkflowTransactionHistoryRepository;
import com.org.fms.mongo.repository.WorkflowTransactionRepository;

@Service
public class StartWorkflowService {

    private final WorkflowTransactionRepository workflowTransactionRepository;
    private final WorkflowTransactionHistoryRepository historyRepository;
    private final WorkflowRepository workflowRepository;
    private final StageTransactionRepository stageTransactionRepository;
    private final WorkflowStageMappingRepository workflowStageMappingRepository;

    public StartWorkflowService(WorkflowTransactionRepository workflowTransactionRepository,
                                WorkflowTransactionHistoryRepository historyRepository,
                                WorkflowRepository workflowRepository,
                                StageTransactionRepository stageTransactionRepository,
                                WorkflowStageMappingRepository workflowStageMappingRepository) {
        this.workflowTransactionRepository = workflowTransactionRepository;
        this.historyRepository = historyRepository;
        this.workflowRepository = workflowRepository;
        this.stageTransactionRepository = stageTransactionRepository;
        this.workflowStageMappingRepository = workflowStageMappingRepository;
    }

    public Map<String, Object> startWorkflow(StartWorkflowRequest request) {
        Map<String, Object> response = new HashMap<>();

        // ✅ Check if transaction already exists
        if (workflowTransactionRepository.findByTransactionId(request.getTransactionId()).isPresent()) {
            response.put("error", "Transaction already exists");
            return response;
        }

        // ✅ Validate if workflow exists
        Optional<Workflow> workflowOpt = workflowRepository.findByWorkflowName(request.getWorkflowName());
        if (!workflowOpt.isPresent()) {
            response.put("error", "Invalid Workflow Name: " + request.getWorkflowName());
            return response;
        }

        Workflow workflow = workflowOpt.get();

        // ✅ Fetch the first stage for this workflow
        List<WorkflowStageMapping> workflowStageMapping = workflowStageMappingRepository.findByWorkflowId(workflow.getWorkflowId());
        if (workflowStageMapping.isEmpty()) {
            response.put("error", "No stages defined for workflow " + workflow.getWorkflowId());
            return response;
        }

        // ✅ Get the first stage (smallest sequence order)
        Integer firstStageId = workflowStageMapping.get(0).getStages().stream()
                .min(Comparator.naturalOrder()) // Sort by smallest stageId
                .orElse(null);

        if (firstStageId == null) {
            response.put("error", "Unable to determine the first stage for workflow " + workflow.getWorkflowId());
            return response;
        }

        // ✅ Fetch the first stage transaction
        Optional<StageTransaction> firstStageTransactionOpt = stageTransactionRepository.findByStageId(firstStageId);
        if (!firstStageTransactionOpt.isPresent()) {
            response.put("error", "No valid stage transaction found for stage " + firstStageId);
            return response;
        }

        StageTransaction firstStageTransaction = firstStageTransactionOpt.get();

        // ✅ Fetch allowed and restricted actions for the first stage
        List<String> allowedActions = firstStageTransaction.getAllowedActions() != null ? firstStageTransaction.getAllowedActions() : List.of();
        List<String> notAllowedActions = firstStageTransaction.getNotAllowedActions() != null ? firstStageTransaction.getNotAllowedActions() : List.of();

        // ✅ Create metadata for tracking
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("initiator", "SYSTEM");
        metadata.put("requestSource", "CLIENT");
        metadata.put("requestTimestamp", Instant.now());

        // ✅ Create a new workflow transaction
        WorkflowTransaction transaction = new WorkflowTransaction(
                request.getTransactionId(),
                workflowStageMapping.get(0).getAppId(),
                workflowStageMapping.get(0).getWorkflowId(),
                firstStageTransaction.getStageId().toString(),
                null, // No previous stage at the start
                "OPEN",
                Instant.now(),
                Instant.now(),
                metadata,
                allowedActions,
                notAllowedActions
        );
        workflowTransactionRepository.save(transaction);

        // ✅ Save workflow transaction history
        WorkflowTransactionHistory history = new WorkflowTransactionHistory(
                request.getTransactionId(),
                workflowStageMapping.get(0).getAppId(),
                workflowStageMapping.get(0).getWorkflowId(),
                workflow.getWorkflowName(),
                null,  // No previous stage at the start
                firstStageTransaction.getStageId().toString(),
                "START",
                "SUCCESS",
                metadata
        );
        historyRepository.save(history);

        // ✅ Response object
        response.put("message", "Workflow started successfully");
        response.put("transactionId", request.getTransactionId());
        response.put("appId", workflowStageMapping.get(0).getAppId());
        response.put("workflowId", workflow.getWorkflowId());
        response.put("workflowName", workflow.getWorkflowName());
        response.put("currentStage", firstStageTransaction.getStageId());
        response.put("status", "OPEN");
        response.put("metadata", metadata);
        response.put("allowedActions", allowedActions);
        response.put("notAllowedActions", notAllowedActions);

        return response;
    }

    /**
     * ✅ Close Workflow by Transaction ID and Workflow ID
     */
    public Map<String, Object> closeWorkflow(StartWorkflowRequest request) {
        Map<String, Object> response = new HashMap<>();

        Optional<WorkflowTransaction> transactionOpt = workflowTransactionRepository.findByTransactionId(request.getTransactionId());
        if (!transactionOpt.isPresent()) {
            response.put("error", "Transaction not found for ID: " + request.getTransactionId());
            return response;
        }

        WorkflowTransaction transaction = transactionOpt.get();

        if (!transaction.getWorkflowId().equals(request.getWorkflowId())) {
            response.put("error", "Transaction does not belong to Workflow ID: " + request.getWorkflowId());
            return response;
        }

        // ✅ Update workflow status to CLOSED
        transaction.setStatus("CLOSED");
        transaction.setUpdatedAt(Instant.now());
        workflowTransactionRepository.save(transaction);

        // ✅ Record in transaction history
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("closedBy", "SYSTEM");
        metadata.put("closedAt", Instant.now());

        WorkflowTransactionHistory history = new WorkflowTransactionHistory(
        		request.getTransactionId(),
                transaction.getAppId(),
                request.getWorkflowId(),
                "Workflow Closed",
                transaction.getCurrentStage(),
                null,
                "CLOSE",
                "SUCCESS",
                metadata
        );
        historyRepository.save(history);

        // ✅ Response object
        response.put("message", "Workflow closed successfully");
        response.put("transactionId", request.getTransactionId());
        response.put("workflowId", request.getWorkflowId());
        response.put("status", "CLOSED");
        response.put("metadata", metadata);

        return response;
    }
}

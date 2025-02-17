package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "workflow_transactions")
public class WorkflowTransaction {
    @Id
    private String id;
    private String transactionId;
    private String appId; // Reference to Application
    private String workflowId; // Reference to Workflow
    private String currentStage;
    private String previousStage;
    private String status; // OPEN, CLOSED, IN_PROGRESS, FAILED
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata; // Stores conditions, history, etc.
    private List<String> allowedActions; // Actions available at the current stage
    private List<String> notAllowedActions; // Restricted actions

    // ✅ No-args constructor for MongoDB
    public WorkflowTransaction() {}

    // ✅ All-args constructor
    public WorkflowTransaction(String transactionId, String appId, String workflowId,
                               String currentStage, String previousStage, String status,
                               Instant createdAt, Instant updatedAt, Map<String, Object> metadata,
                               List<String> allowedActions, List<String> notAllowedActions) {
        this.transactionId = transactionId;
        this.appId = appId;
        this.workflowId = workflowId;
        this.currentStage = currentStage;
        this.previousStage = previousStage;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
        this.metadata = metadata;
        this.allowedActions = allowedActions;
        this.notAllowedActions = notAllowedActions;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getWorkflowId() { return workflowId; }
    public String getAppId() { return appId; }
    public String getCurrentStage() { return currentStage; }
    public String getPreviousStage() { return previousStage; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Map<String, Object> getMetadata() { return metadata; }
    public List<String> getAllowedActions() { return allowedActions; }
    public List<String> getNotAllowedActions() { return notAllowedActions; }

    // ✅ Setters
    public void setId(String id) { this.id = id; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setAppId(String appId) { this.appId = appId; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }
    public void setPreviousStage(String previousStage) { this.previousStage = previousStage; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setAllowedActions(List<String> allowedActions) { this.allowedActions = allowedActions; }
    public void setNotAllowedActions(List<String> notAllowedActions) { this.notAllowedActions = notAllowedActions; }

    // ✅ toString() for Logging
    @Override
    public String toString() {
        return "WorkflowTransaction{" +
                "id='" + id + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", appId='" + appId + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", currentStage='" + currentStage + '\'' +
                ", previousStage='" + previousStage + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", metadata=" + metadata +
                ", allowedActions=" + allowedActions +
                ", notAllowedActions=" + notAllowedActions +
                '}';
    }

    /*
    {
        "id": "TXN12345",
        "transactionId": "TRX-INS-001",
        "workflowId": "WF001",
        "currentStage": "Quote Verification",
        "previousStage": "Initiation",
        "status": "OPEN",
        "createdAt": "2024-02-10T12:45:00Z",
        "updatedAt": "2024-02-10T12:50:00Z",
        "metadata": {
            "approvalRequired": true,
            "assignedTo": "User123"
        },
        "allowedActions": ["VALIDATE_QUOTE", "APPROVE_QUOTE"],
        "notAllowedActions": ["REJECT_QUOTE"]
    }
    */
}

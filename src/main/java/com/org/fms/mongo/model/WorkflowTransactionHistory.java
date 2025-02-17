package com.org.fms.mongo.model;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "workflow_transaction_history")
public class WorkflowTransactionHistory {

    @Id
    private String id;
    private String transactionId;
    private String appId; // ✅ Reference to appId
    private String workflowId; // ✅ Reference to Workflow
    private String workflowName;
    private String fromStage;
    private String toStage;
    private String action;
    private String status; // ✅ SUCCESS, FAILED, ROLLBACK
    private Instant timestamp;
    private Map<String, Object> metadata; // ✅ Stores additional attributes like conditions, approvals

    // ✅ No-args constructor for MongoDB
    public WorkflowTransactionHistory() {}

    // ✅ All-args constructor
    public WorkflowTransactionHistory(String transactionId,String appId, String workflowId, String workflowName,
                                      String fromStage, String toStage, String action, 
                                      String status, Map<String, Object> metadata) {
        this.transactionId = transactionId;
        this.appId = appId;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.fromStage = fromStage;
        this.toStage = toStage;
        this.action = action;
        this.status = status;
        this.timestamp = Instant.now();
        this.metadata = metadata;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getWorkflowId() { return workflowId; }
    public String getAppId() { return appId; }
    public String getWorkflowName() { return workflowName; }
    public String getFromStage() { return fromStage; }
    public String getToStage() { return toStage; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getMetadata() { return metadata; }

    // ✅ Setters
    public void setId(String id) { this.id = id; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setAppId(String appId) { this.appId = appId; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    public void setFromStage(String fromStage) { this.fromStage = fromStage; }
    public void setToStage(String toStage) { this.toStage = toStage; }
    public void setAction(String action) { this.action = action; }
    public void setStatus(String status) { this.status = status; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // ✅ toString() for Logging
    @Override
    public String toString() {
        return "WorkflowTransactionHistory{" +
                "id='" + id + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", appId='" + appId + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", fromStage='" + fromStage + '\'' +
                ", toStage='" + toStage + '\'' +
                ", action='" + action + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", metadata=" + metadata +
                '}';
    }
    
    
   /* {
        "id": "HIST-TRX-001",
        "transactionId": "TRX-INS-001",
        "workflowId": "WF001",
        "workflowName": "Policy Workflow",
        "fromStage": "Quote Verification",
        "toStage": "Quote Approval",
        "action": "APPROVE_QUOTE",
        "status": "SUCCESS",
        "timestamp": "2024-02-10T12:50:00Z",
        "metadata": {
            "approvedBy": "User123",
            "approvalComment": "All validations passed"
        }
    } */

}

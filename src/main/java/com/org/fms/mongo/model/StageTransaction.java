package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "stage_transactions")
public class StageTransaction {

    @Id
    private String id;
    private String appId;
    private String workflowId;
    private String transactionId;
    private Integer stageId;
    private String stageName;
    private List<StageTransition> nextStages;
    private List<StageTransition> previousStages;
    private List<StageTransition> rollbackStages;
    private List<StageTransition> jumpStages;
    private List<StageTransition> skipStages;
    private List<String> allowedActions;
    private List<String> notAllowedActions;
    private String action;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata;

    public StageTransaction() {}

    public StageTransaction(String appId, String workflowId,String transactionId,Integer stageId, String stageName,
                            List<StageTransition> nextStages, List<StageTransition> previousStages,
                            List<StageTransition> rollbackStages, List<StageTransition> jumpStages, List<StageTransition> skipStages,
                            List<String> allowedActions, List<String> notAllowedActions, String action, String status,
                            Instant createdAt, Instant updatedAt, Map<String, Object> metadata) {
        this.appId = appId;
        this.workflowId = workflowId;
        this.transactionId = transactionId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.nextStages = nextStages;
        this.previousStages = previousStages;
        this.rollbackStages = rollbackStages;
        this.jumpStages = jumpStages;
        this.skipStages = skipStages;
        this.allowedActions = allowedActions;
        this.notAllowedActions = notAllowedActions;
        this.action = action;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
        this.metadata = metadata;
    }

    public String getId() { return id; }
    public String getAppId() { return appId; }
    public String getWorkflowId() { return workflowId; }
    public String getTransactionId() { return transactionId; }
    public Integer getStageId() { return stageId; }
    public String getStageName() { return stageName; }
    public List<StageTransition> getNextStages() { return nextStages; }
    public List<StageTransition> getPreviousStages() { return previousStages; }
    public List<StageTransition> getRollbackStages() { return rollbackStages; }
    public List<StageTransition> getJumpStages() { return jumpStages; }
    public List<StageTransition> getSkipStages() { return skipStages; }
    public List<String> getAllowedActions() { return allowedActions; }
    public List<String> getNotAllowedActions() { return notAllowedActions; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Map<String, Object> getMetadata() { return metadata; }

    public void setId(String id) { this.id = id; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setAppId(String appId) { this.appId = appId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setStageId(Integer stageId) { this.stageId = stageId; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public void setNextStages(List<StageTransition> nextStages) { this.nextStages = nextStages; }
    public void setPreviousStages(List<StageTransition> previousStages) { this.previousStages = previousStages; }
    public void setRollbackStages(List<StageTransition> rollbackStages) { this.rollbackStages = rollbackStages; }
    public void setJumpStages(List<StageTransition> jumpStages) { this.jumpStages = jumpStages; }
    public void setSkipStages(List<StageTransition> skipStages) { this.skipStages = skipStages; }
    public void setAllowedActions(List<String> allowedActions) { this.allowedActions = allowedActions; }
    public void setNotAllowedActions(List<String> notAllowedActions) { this.notAllowedActions = notAllowedActions; }
    public void setAction(String action) { this.action = action; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "StageTransaction{" +
                "id='" + id + '\'' +
                ", appId='" + appId + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", nextStages=" + nextStages +
                ", previousStages=" + previousStages +
                ", rollbackStages=" + rollbackStages +
                ", jumpStages=" + jumpStages +
                ", skipStages=" + skipStages +
                ", allowedActions=" + allowedActions +
                ", notAllowedActions=" + notAllowedActions +
                ", action='" + action + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", metadata=" + metadata +
                '}';
    }
}

package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "workflow_actions")
public class WorkflowAction {
    @Id
    private String id;
    private String workflowId; // Reference to Workflow
    private String actionCode;
    private String actionName;
    private String category; // SYSTEM, APPLICATION, or CUSTOM
    private String description;
    private String genericActionId; // Reference to GenericAction
    private List<String> allowedStages; // Allowed stages for this action
    private List<String> notAllowedStages; // Stages where this action is restricted
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata; // Additional attributes (rules, conditions, etc.)

    // ✅ No-args constructor for MongoDB
    public WorkflowAction() {}

    // ✅ All-args constructor
    public WorkflowAction(String workflowId, String actionCode, String actionName, String category,
                          String description, String genericActionId, List<String> allowedStages,
                          List<String> notAllowedStages, Instant createdAt, Instant updatedAt, Map<String, Object> metadata) {
        this.workflowId = workflowId;
        this.actionCode = actionCode;
        this.actionName = actionName;
        this.category = category;
        this.description = description;
        this.genericActionId = genericActionId;
        this.allowedStages = allowedStages;
        this.notAllowedStages = notAllowedStages;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
        this.metadata = metadata;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getWorkflowId() { return workflowId; }
    public String getActionCode() { return actionCode; }
    public String getActionName() { return actionName; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getGenericActionId() { return genericActionId; }
    public List<String> getAllowedStages() { return allowedStages; }
    public List<String> getNotAllowedStages() { return notAllowedStages; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Map<String, Object> getMetadata() { return metadata; }

    // ✅ Setters
    public void setId(String id) { this.id = id; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setActionCode(String actionCode) { this.actionCode = actionCode; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setGenericActionId(String genericActionId) { this.genericActionId = genericActionId; }
    public void setAllowedStages(List<String> allowedStages) { this.allowedStages = allowedStages; }
    public void setNotAllowedStages(List<String> notAllowedStages) { this.notAllowedStages = notAllowedStages; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // ✅ toString() for Logging
    @Override
    public String toString() {
        return "WorkflowAction{" +
                "id='" + id + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", actionName='" + actionName + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", genericActionId='" + genericActionId + '\'' +
                ", allowedStages=" + allowedStages +
                ", notAllowedStages=" + notAllowedStages +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", metadata=" + metadata +
                '}';
    }
    /*
    {
        "id": "ACT123",
        "workflowId": "WF001",
        "actionCode": "APPROVE_QUOTE",
        "actionName": "Approve Quote",
        "category": "APPLICATION",
        "description": "Approves the quote after validation",
        "genericActionId": "GEN123",
        "allowedStages": ["Quote Verification", "Quote Approval"],
        "notAllowedStages": ["Initiation"],
        "createdAt": "2024-02-10T12:45:00Z",
        "updatedAt": "2024-02-10T12:50:00Z",
        "metadata": {
            "approvalLevel": "Senior",
            "requiresManagerApproval": true
        }
    } */

}

package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "generic_actions")
public class GenericAction {
    @Id
    private String id;
    private String actionCode;
    private String name;
    private String category;  // SYSTEM or APPLICATION
    private String description;
    
    private List<String> relatedWorkflows;  // Graph traversal: Workflows where this action applies
    private Boolean requiresApproval;  // Indicates if manual approval is required
    private Boolean isAutoExecutable;  // Specifies if the action runs automatically

    // ✅ No-args constructor
    public GenericAction() {}

    // ✅ All-args constructor (Removed parentActionId)
    public GenericAction(String actionCode, String name, String category, String description, 
                         List<String> relatedWorkflows, Boolean requiresApproval, Boolean isAutoExecutable) {
        this.actionCode = actionCode;
        this.name = name;
        this.category = category;
        this.description = description;
        this.relatedWorkflows = relatedWorkflows;
        this.requiresApproval = requiresApproval;
        this.isAutoExecutable = isAutoExecutable;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getActionCode() { return actionCode; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public List<String> getRelatedWorkflows() { return relatedWorkflows; }
    public Boolean getRequiresApproval() { return requiresApproval; }
    public Boolean getIsAutoExecutable() { return isAutoExecutable; }

    // ✅ Setters
    public void setRelatedWorkflows(List<String> relatedWorkflows) { this.relatedWorkflows = relatedWorkflows; }
    public void setRequiresApproval(Boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    public void setIsAutoExecutable(Boolean isAutoExecutable) { this.isAutoExecutable = isAutoExecutable; }

    // ✅ toString() for Logging
    @Override
    public String toString() {
        return "GenericAction{" +
                "id='" + id + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", relatedWorkflows=" + relatedWorkflows +
                ", requiresApproval=" + requiresApproval +
                ", isAutoExecutable=" + isAutoExecutable +
                '}';
    }
    
    /*
    ✅ Example JSON without Parent-Child hierarchy:
    {
        "id": "GA1",
        "actionCode": "APPROVE_QUOTE",
        "name": "Approve Quote",
        "category": "APPLICATION",
        "description": "Approve a verified quote",
        "relatedWorkflows": ["Quote Validation", "Underwriting"],
        "requiresApproval": true,
        "isAutoExecutable": false
    }
    */

}

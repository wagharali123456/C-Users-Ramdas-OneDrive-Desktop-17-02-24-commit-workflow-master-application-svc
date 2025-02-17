package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "workflows")
public class Workflow {
    @Id
    private String id;
    private String workflowId;
    private String workflowName;
    private String description;
	private boolean saveStagePageData; // Enable/Disable Page Data Saving
    private Instant createdAt;
    private Instant updatedAt;

    public Workflow() {}

    public Workflow(String workflowId, String workflowName, String description,boolean saveStagePageData) {
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.description = description;
        this.saveStagePageData = saveStagePageData;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getWorkflowId() { return workflowId; }
    public String getWorkflowName() { return workflowName; }
    public String getDescription() { return description; }
    public boolean isSaveStagePageData() { return saveStagePageData; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(String id) { this.id = id; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    public void setDescription(String description) { this.description = description; }
	public void setSaveStagePageData(boolean saveStagePageData) { this.saveStagePageData = saveStagePageData;}
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Workflow{" +
                "id='" + id + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", description='" + description + '\'' +
                ", saveStagePageData='" + saveStagePageData + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

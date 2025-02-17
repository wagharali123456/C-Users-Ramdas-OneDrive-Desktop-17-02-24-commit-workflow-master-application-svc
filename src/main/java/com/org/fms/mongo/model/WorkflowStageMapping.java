package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "workflow_stage_mapping")
public class WorkflowStageMapping {

    @Id
    private String id;
    private String workflowId;
    private String appId;
    private List<Integer> stages;

    public WorkflowStageMapping() {}

    public WorkflowStageMapping(String workflowId, String appId, List<Integer> stages) {
        this.workflowId = workflowId;
        this.appId = appId;
        this.stages = stages;
    }

    public String getId() { return id; }
    public String getWorkflowId() { return workflowId; }
    public String getAppId() { return appId; }
    public List<Integer> getStages() { return stages; }

    public void setId(String id) { this.id = id; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setAppId(String appId) { this.appId = appId; }
    public void setStages(List<Integer> stages) { this.stages = stages; }

    @Override
    public String toString() {
        return "WorkflowStageMapping{" +
                "id='" + id + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", appId='" + appId + '\'' +
                ", stages=" + stages +
                '}';
    }
}

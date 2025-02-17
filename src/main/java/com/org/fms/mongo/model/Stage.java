package com.org.fms.mongo.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stages")
public class Stage {

    @Id
    private String id;
    private int stageId;   
    private String name;
    private TimeoutConfig timeoutConfig;
    private Instant createdAt;
    private Instant updatedAt;

    public Stage() {}

    public Stage(int stageId,String name,TimeoutConfig timeoutConfig) {
    	this.stageId = stageId; 
        this.name = name;
        this.timeoutConfig = timeoutConfig;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public int getStageId() { return stageId; }
    public String getName() { return name; }
    public TimeoutConfig getTimeoutConfig() { return timeoutConfig; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setStageId(int stageId) { this.stageId = stageId; }
    public void setTimeoutConfig(TimeoutConfig timeoutConfig) { this.timeoutConfig = timeoutConfig; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Stage{" +
                "id='" + id + '\'' +
                 ", stageId='" + stageId + '\'' +
                ", name='" + name + '\'' +
                ", timeoutConfig=" + timeoutConfig +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

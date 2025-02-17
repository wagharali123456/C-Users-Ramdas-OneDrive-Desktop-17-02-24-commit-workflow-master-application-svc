package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Document(collection = "stage_page_data")
public class PageData {

    @Id
    private String id;
    private String transactionId;
    private String stageId;
    private Map<String, Object> pageData;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean reopened;
    private boolean isTimedOut;
    private String fallbackStageId;  // Reference to fallback stage in case of timeout

    public PageData(String transactionId, String stageId, Map<String, Object> pageData) {
        this.transactionId = transactionId;
        this.stageId = stageId;
        this.pageData = pageData;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.reopened = false;
        this.isTimedOut = false;
        this.fallbackStageId = null;  // Default: No fallback
    }

    public String getTransactionId() { return transactionId; }
    public String getStageId() { return stageId; }
    public Map<String, Object> getPageData() { return pageData; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public boolean isReopened() { return reopened; }
    public boolean isTimedOut() { return isTimedOut; }
    public String getFallbackStageId() { return fallbackStageId; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public void setPageData(Map<String, Object> pageData) { 
        this.pageData = pageData; 
        this.updatedAt = Instant.now();  // Update timestamp when data is modified
    }
    public void setReopened(boolean reopened) { this.reopened = reopened; }
    public void setTimedOut(boolean timedOut) { this.isTimedOut = timedOut; }
    public void setFallbackStageId(String fallbackStageId) { this.fallbackStageId = fallbackStageId; }
}

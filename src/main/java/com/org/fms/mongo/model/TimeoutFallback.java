package com.org.fms.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeoutFallback {

    @JsonProperty("fallbackStageId")
    private Integer fallbackStageId;

    public TimeoutFallback() {}

    public TimeoutFallback(Integer fallbackStageId) {
        this.fallbackStageId = fallbackStageId;
    }

    public Integer getFallbackStageId() { return fallbackStageId; }

    public void setFallbackStageId(Integer fallbackStageId) { this.fallbackStageId = fallbackStageId; }

    @Override
    public String toString() {
        return "TimeoutFallback{" +
                "fallbackStageId=" + fallbackStageId +
                '}';
    }
}

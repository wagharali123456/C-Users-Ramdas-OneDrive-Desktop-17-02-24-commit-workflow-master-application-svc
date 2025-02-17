package com.org.fms.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class TimeoutConfig {
    @JsonProperty("timeoutEnabled")
    private boolean timeoutEnabled;

    @JsonProperty("timeoutSeconds")
    private int timeoutSeconds;

    @JsonProperty("timeoutFallback")
    private Map<String, Integer> timeoutFallback;

    public TimeoutConfig() {}

    public TimeoutConfig(boolean timeoutEnabled, int timeoutSeconds, Map<String, Integer> timeoutFallback) {
        this.timeoutEnabled = timeoutEnabled;
        this.timeoutSeconds = timeoutSeconds;
        this.timeoutFallback = timeoutFallback;
    }

    public boolean isTimeoutEnabled() { return timeoutEnabled; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public Integer getFallbackStageId() { 
        return timeoutFallback != null ? timeoutFallback.get("fallbackStageId") : null; 
    }

    public void setTimeoutEnabled(boolean timeoutEnabled) { this.timeoutEnabled = timeoutEnabled; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public void setTimeoutFallback(Map<String, Integer> timeoutFallback) { this.timeoutFallback = timeoutFallback; }

    @Override
    public String toString() {
        return "TimeoutConfig{" +
                "timeoutEnabled=" + timeoutEnabled +
                ", timeoutSeconds=" + timeoutSeconds +
                ", timeoutFallback=" + timeoutFallback +
                '}';
    }
}

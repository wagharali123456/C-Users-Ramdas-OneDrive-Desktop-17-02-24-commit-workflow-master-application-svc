package com.org.fms.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StageTransition {

    @JsonProperty("action")
    private String action;

    @JsonProperty("targetStageIds")
    private List<Integer> targetStageIds;

    public StageTransition() {}

    public StageTransition(String action, List<Integer> targetStageIds) {
        this.action = action;
        this.targetStageIds = targetStageIds;
    }

    public String getAction() { return action; }
    public List<Integer> getTargetStageIds() { return targetStageIds; }

    public void setAction(String action) { this.action = action; }
    public void setTargetStageIds(List<Integer> targetStageIds) { this.targetStageIds = targetStageIds; }

    @Override
    public String toString() {
        return "StageTransition{" +
                "action='" + action + '\'' +
                ", targetStageIds=" + targetStageIds +
                '}';
    }
}

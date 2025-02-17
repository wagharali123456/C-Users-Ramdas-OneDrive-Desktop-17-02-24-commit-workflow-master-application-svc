///*package com.org.fms.mongo.model;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import java.util.List;
//
//@Document(collection = "actions")
//public class Action {
//    @Id
//    private String id;
//    private String appId;
//    private String appCode;
//    private String workflowId;
//    private String stageId;
//
//    private List<String> allowedActions;    // Actions permitted for this workflow-stage
//    private List<String> notAllowedActions; // Actions restricted at this stage
//
//    private String actionType;  // SYSTEM or APPLICATION
//    private List<String> relatedWorkflows;  // Graph traversal → Workflows where this action is valid
//
//    // ✅ No-args constructor for MongoDB
//    public Action() {}
//
//    // ✅ All-args constructor (Removed `parentActionId`)
//    public Action(String appId,String appCode, String workflowId, String stageId, String actionType,
//                  List<String> allowedActions, List<String> notAllowedActions, List<String> relatedWorkflows) {
//        this.appId = appId;
//        this.appCode = appCode;
//        this.workflowId = workflowId;
//        this.stageId = stageId;
//        this.actionType = actionType;
//        this.allowedActions = allowedActions;
//        this.notAllowedActions = notAllowedActions;
//        this.relatedWorkflows = relatedWorkflows;
//    }
//
//    // ✅ Getters
//    public String getId() { return id; }
//    public String getAppId() { return appId; }
//    public String getAppCode() { return appCode; }
//    public String getWorkflowId() { return workflowId; }
//    public String getStageId() { return stageId; }
//    public String getActionType() { return actionType; }
//    public List<String> getAllowedActions() { return allowedActions; }
//    public List<String> getNotAllowedActions() { return notAllowedActions; }
//    public List<String> getRelatedWorkflows() { return relatedWorkflows; }
//
//    // ✅ Setters
//    public void setAllowedActions(List<String> allowedActions) { this.allowedActions = allowedActions; }
//    public void setNotAllowedActions(List<String> notAllowedActions) { this.notAllowedActions = notAllowedActions; }
//    public void setRelatedWorkflows(List<String> relatedWorkflows) { this.relatedWorkflows = relatedWorkflows; }
//
//    // ✅ toString() for Logging
//    @Override
//    public String toString() {
//        return "Action{" +
//                "id='" + id + '\'' +
//                ", appId='" + appId + '\'' +
//                ", appCode='" + appCode + '\'' +
//                ", workflowId='" + workflowId + '\'' +
//                ", stageId='" + stageId + '\'' +
//                ", actionType='" + actionType + '\'' +
//                ", allowedActions=" + allowedActions +
//                ", notAllowedActions=" + notAllowedActions +
//                ", relatedWorkflows=" + relatedWorkflows +
//                '}';
//    }
//
//    /*
//    ✅ Example JSON Without Parent-Child Hierarchy:
//    {
//        "id": "A1",
//        "appId": "INSURANCE_APP",
//        "workflowId": "Policy Workflow",
//        "stageId": "Quote Validation",
//        "actionType": "APPLICATION",
//        "allowedActions": ["VALIDATE_QUOTE", "APPROVE_QUOTE"],
//        "notAllowedActions": ["CANCEL_QUOTE"],
//        "relatedWorkflows": ["Lead Creation", "Underwriting"]
//    }
//    */
//} */

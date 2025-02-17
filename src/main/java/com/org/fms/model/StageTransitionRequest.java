package com.org.fms.model;

import java.util.Map;

public class StageTransitionRequest {

	private String workflowId;
	private String appId;
	private String transactionId;
	private String action;
	private Map<String, Object> pageData;
	private int toStageId;

	// Constructors
	public StageTransitionRequest() {
	}

	public StageTransitionRequest(String workflowId, String appId, String transactionId, String action,
			Map<String, Object> pageData, int toStageId) {
		this.workflowId = workflowId;
		this.appId = appId;
		this.transactionId = transactionId;
		this.action = action;
		this.pageData = pageData;
		this.toStageId = toStageId;
	}

	// Getters and Setters
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String, Object> pageData) {
		this.pageData = pageData;
	}

	public int getToStageId() {
		return toStageId;
	}

	public void setToStageId(int toStageId) {
		this.toStageId = toStageId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}

package com.org.fms.mongo.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.org.fms.model.StageTransitionRequest;
import com.org.fms.mongo.model.Application;
import com.org.fms.mongo.model.GenericAction;
import com.org.fms.mongo.model.PageData;
import com.org.fms.mongo.model.Stage;
import com.org.fms.mongo.model.StageTransaction;
import com.org.fms.mongo.model.StageTransition;
import com.org.fms.mongo.model.Workflow;
import com.org.fms.mongo.model.WorkflowTransaction;
import com.org.fms.mongo.model.WorkflowTransactionHistory;
import com.org.fms.mongo.repository.ApplicationRepository;
import com.org.fms.mongo.repository.PageDataRepository;
import com.org.fms.mongo.repository.StageRepository;
import com.org.fms.mongo.repository.StageTransactionRepository;
import com.org.fms.mongo.repository.WorkflowRepository;
import com.org.fms.mongo.repository.WorkflowTransactionHistoryRepository;
import com.org.fms.mongo.repository.WorkflowTransactionRepository;

@Service
public class StageTransactionService {

	private final StageRepository stageRepository;
	private final StageTransactionRepository stageTransactionRepository;
	private final WorkflowTransactionRepository workflowTransactionRepository;
	private final WorkflowTransactionHistoryRepository historyRepository;
	private final WorkflowRepository workflowRepository;
	private final PageDataRepository pageDataRepository;
	private final GenericActionService genericActionService;
	private final ApplicationRepository applicationRepository;

	public StageTransactionService(StageRepository stageRepository,
			StageTransactionRepository stageTransactionRepository,
			WorkflowTransactionRepository workflowTransactionRepository,
			WorkflowTransactionHistoryRepository historyRepository, WorkflowRepository workflowRepository,
			PageDataRepository pageDataRepository, GenericActionService genericActionService,
			ApplicationRepository applicationRepository) {
		this.stageRepository = stageRepository;
		this.stageTransactionRepository = stageTransactionRepository;
		this.workflowTransactionRepository = workflowTransactionRepository;
		this.historyRepository = historyRepository;
		this.workflowRepository = workflowRepository;
		this.pageDataRepository = pageDataRepository;
		this.genericActionService = genericActionService;
		this.applicationRepository = applicationRepository;
	}

	public Map<String, Object> processStageTransition(StageTransitionRequest request) {
		Map<String, Object> response = new HashMap<>();
		// Validate Action
		Optional<GenericAction> actionOpt = genericActionService.getActionByCode(request.getAction());
		if (!actionOpt.isPresent()) {
			response.put("message", "Invalid action: " + request.getAction());
			response.put("status", "FAILED");
			return response;
		}

		// ✅ Validate if workflow exists
		Optional<Workflow> workflowOpt = workflowRepository.findByWorkflowId(request.getWorkflowId());
		if (!workflowOpt.isPresent()) {
			response.put("error", "Invalid Workflow Id: " + request.getWorkflowId());
			return response;
		}

		// ✅ Validate if Application Id exists
		Optional<Application> applicationOpt = applicationRepository.findByAppId(request.getAppId());
		if (!applicationOpt.isPresent()) {
			response.put("error", "Invalid Application Id: " + request.getAppId());
			return response;
		}

		Workflow workflow = workflowOpt.get();

		// Validate Workflow Transaction
		Optional<WorkflowTransaction> workflowTransactionOpt = workflowTransactionRepository
				.findByTransactionIdAndAppIdAndWorkflowId(request.getTransactionId(),request.getAppId(), request.getWorkflowId());
		if (!workflowTransactionOpt.isPresent()) {
			response.put("message", "Transaction ID not found!");
			response.put("transactionId", request.getTransactionId());
			response.put("status", "FAILED");
			return response;
		}

		WorkflowTransaction workflowTransaction = workflowTransactionOpt.get();
		int currentStageId = Integer.parseInt(workflowTransaction.getCurrentStage());

		// Validate Stage Transaction
		Optional<StageTransaction> stageTransactionOpt = stageTransactionRepository.findByStageId(currentStageId);
		if (!stageTransactionOpt.isPresent()) {
			response.put("message", "Stage transaction not found!");
			response.put("status", "FAILED");
			return response;
		}

		StageTransaction currentStageTransaction = stageTransactionOpt.get();
		if (!currentStageTransaction.getAllowedActions().contains(request.getAction())) {
			response.put("message", "Action not allowed for the current stage.");
			response.put("status", "FAILED");
			return response;
		}

		System.out.println("📌 Current Stage ID: " + currentStageId);
		System.out.println("📌 Transaction ID: " + request.getTransactionId());
		System.out.println("📌 stageTransactionOpt : " + stageTransactionOpt);
		if (!stageTransactionOpt.isPresent()) {
			response.put("message", "Stage transaction not found!");
			response.put("status", "FAILED");
			return response;
		}

		System.out.println("✅ Allowed Actions: " + currentStageTransaction.getAllowedActions());

		// ✅ Fetch timeout config from Stage entity
		Optional<Stage> stageOpt = stageRepository.findByStageId(currentStageId);
		if (!stageOpt.isPresent()) {
			response.put("message", "Stage not found in the database!");
			response.put("status", "FAILED");
			return response;
		}
		Stage currentStage = stageOpt.get();
		boolean saveStagePageData = workflow.isSaveStagePageData();

		// ✅ Handle Timeout Configuration
		handleTimeoutScenario(workflowTransaction, currentStage);

		// ✅ Handle ROLLBACK
		if ("ROLLBACK".equalsIgnoreCase(request.getAction())) {
			response.put("message",
					handleRollback(workflowTransaction, currentStageTransaction, request.getTransactionId()));
			response.put("status", "SUCCESS");
			return response;
		}

		// ✅ Handle JUMP
		if ("JUMP".equalsIgnoreCase(request.getAction())) {
			response.put("message", handleJump(workflowTransaction, currentStageTransaction, request.getTransactionId(),
					request.getToStageId()));
			response.put("status", "SUCCESS");
			return response;
		}

		// ✅ Save or Update Page Data for Reopened Stages (Only if Enabled)
		if (saveStagePageData) {
			saveOrUpdatePageData(request.getTransactionId(), currentStageId, request.getPageData());
		} else {
			System.out.println("⚠️ Page Data NOT saved as per workflow configuration.");
		}

		// ✅ Get the Next Stage ID dynamically based on action
		int nextStageId = getNextStageId(request.getAction(), currentStageTransaction);
		if (nextStageId == -1) {
			response.put("message", "No valid transition found for action: " + request.getAction());
			response.put("status", "FAILED");
			return response;
		}

		Optional<StageTransaction> nextStageOpt = stageTransactionRepository.findByStageId(nextStageId);
		if (!nextStageOpt.isPresent()) {
			response.put("message", "Next stage transaction not found!");
			response.put("status", "FAILED");
			return response;
		}

		StageTransaction nextStageTransaction = nextStageOpt.get();

		System.out.println("📝 Stage Transition Saved: " + currentStageId + " → " + nextStageId);

		// ✅ Update previous stage status to CLOSED
		closePreviousStageTransaction(request.getTransactionId(), currentStageId);

		// ✅ Update Workflow Transaction
		updateWorkflowTransaction(workflowTransaction, nextStageTransaction);

		// ✅ Save Transition History
		saveWorkflowTransactionHistory(request.getTransactionId(), workflowTransaction, currentStageId, nextStageId,
				request.getAction());

		// ✅ Check if all stages are CLOSED and close the workflow
		if (isAllStagesClosed(workflowTransaction.getTransactionId())) {
			closeWorkflow(workflowTransaction);
		}

		// ✅ Allowed Actions for the new stage
		List<String> allowedActions = nextStageTransaction.getAllowedActions();
		List<String> notAllowedActions = nextStageTransaction.getNotAllowedActions();

		// ✅ Metadata
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("actor", "System");
		metadata.put("timestamp", Instant.now());

		response.put("message", "Transaction Successful: Moved from " + currentStageId + " to " + nextStageId);
		response.put("transactionId", request.getTransactionId());
		response.put("appId", workflowTransaction.getAppId());
		response.put("workflowId", workflow.getWorkflowId());
		response.put("workflowName", workflow.getWorkflowName());
		response.put("currentStage", nextStageTransaction.getStageId());
		response.put("currentStageName", nextStageTransaction.getStageName());
		response.put("status", "OPEN");
		response.put("metadata", metadata);
		response.put("allowedActions", allowedActions);
		response.put("notAllowedActions", notAllowedActions);
		return response;
	}

	/**
	 * Checks if all stage transactions for a given workflow are CLOSED.
	 */
	private boolean isAllStagesClosed(String transactionId) {
		List<StageTransaction> stageTransactions = stageTransactionRepository.findByTransactionId(transactionId);
		return stageTransactions.stream().allMatch(stage -> "CLOSED".equals(stage.getStatus()));
	}

	/**
	 * Closes the workflow if all stages are CLOSED.
	 */
	private void closeWorkflow(WorkflowTransaction workflowTransaction) {
		workflowTransaction.setStatus("CLOSED");
		workflowTransactionRepository.save(workflowTransaction);
		System.out.println("✅ Workflow Closed: Transaction ID " + workflowTransaction.getTransactionId());
	}

	private void closePreviousStageTransaction(String transactionId, int currentStageId) {
		List<WorkflowTransactionHistory> historylist = historyRepository.findByTransactionIdAndToStage(transactionId,
				String.valueOf(currentStageId));
		if (!historylist.isEmpty()) {
			for (WorkflowTransactionHistory history : historylist) {
				history.setStatus("CLOSED");
			}
			historyRepository.saveAll(historylist); // Save all records after updating status
			System.out.println("✅ Previous stage transactions CLOSED for stage: " + currentStageId);
		} else {
			System.out.println("⚠️ No previous stage transaction found for stage: " + currentStageId);
		}
	}

	/**
	 * Retrieves the next stage ID dynamically based on the given action.
	 */
	private Integer getNextStageId(String action, StageTransaction currentStageTransaction) {
		System.out.println("🔍 Checking next stage for action: " + action);
		System.out.println("✅ Current Stage ID: " + currentStageTransaction.getStageId());
		System.out.println("✅ Allowed Actions: " + currentStageTransaction.getAllowedActions());
		System.out.println("✅ Next Stages: " + currentStageTransaction.getNextStages());

		if (!currentStageTransaction.getAllowedActions().contains(action)) {
			System.out.println(
					"❌ Action [" + action + "] is NOT allowed for stage: " + currentStageTransaction.getStageId());
			return -1;
		}

		Integer nextStageId = -1; // Default invalid stage

		switch (action.toUpperCase()) {
		case "NEXT":
			nextStageId = getStageFromList(currentStageTransaction.getNextStages(), "NEXT");
			if (nextStageId == -1) {
				nextStageId = getDefaultNextStage(currentStageTransaction); // Fallback if no explicit transition
			}
			break;

		case "PREVIOUS":
			nextStageId = getStageFromList(currentStageTransaction.getPreviousStages(), "PREVIOUS");
			if (nextStageId == -1) {
				nextStageId = getDefaultPreviousStage(currentStageTransaction); // Fallback to sequential
			}
			break;

		case "ROLLBACK":
			nextStageId = getStageFromList(currentStageTransaction.getRollbackStages(), "ROLLBACK");
			break;

		case "JUMP":
			nextStageId = getStageFromList(currentStageTransaction.getJumpStages(), "JUMP");
			break;

		default:
			nextStageId = getCustomNextStage(action, currentStageTransaction);
		}

		if (nextStageId == null || nextStageId == -1) {
			System.out.println("⚠️ No valid transition found for action: " + action);
			return -1;
		}

		System.out.println("✅ Next Stage ID: " + nextStageId);
		return nextStageId;
	}

	private Integer getDefaultNextStage(StageTransaction currentStageTransaction) {
		if (currentStageTransaction.getNextStages() == null || currentStageTransaction.getNextStages().isEmpty()) {
			System.out.println("⚠️ No explicitly configured NEXT stages. Using default sequence order.");
			return currentStageTransaction.getStageId() + 1; // Assume next stage is in sequence
		}

		return getStageFromList(currentStageTransaction.getNextStages(), "NEXT");
	}

	private Integer getDefaultPreviousStage(StageTransaction currentStageTransaction) {
		if (currentStageTransaction.getPreviousStages() == null
				|| currentStageTransaction.getPreviousStages().isEmpty()) {
			System.out.println("⚠️ No explicitly configured PREVIOUS stages. Using default sequence order.");
			return currentStageTransaction.getStageId() - 1; // Assume previous stage is in sequence
		}

		return getStageFromList(currentStageTransaction.getPreviousStages(), "PREVIOUS");
	}

	private Integer getStageFromList(List<StageTransition> stageTransitions, String actionType) {
		if (stageTransitions == null || stageTransitions.isEmpty()) {
			System.out.println("⚠️ No valid stage found for action type: " + actionType);
			return -1;
		}

		Optional<StageTransition> transitionOpt = stageTransitions.stream()
				.filter(transition -> transition.getAction().equalsIgnoreCase(actionType)).findFirst();

		if (transitionOpt.isPresent() && transitionOpt.get().getTargetStageIds() != null
				&& !transitionOpt.get().getTargetStageIds().isEmpty()) {
			Integer targetStageId = transitionOpt.get().getTargetStageIds().get(0);
			System.out.println("✅ " + actionType + " → Moving to Stage ID: " + targetStageId);
			return targetStageId;
		}

		System.out.println("⚠️ No valid target stage found for action type: " + actionType);
		return -1;
	}

	/**
	 * Retrieves the correct next stage dynamically based on custom action mapping.
	 */
	private Integer getCustomNextStage(String action, StageTransaction currentStage) {
		if (currentStage.getNextStages() == null || currentStage.getNextStages().isEmpty()) {
			System.out.println("⚠️ No next stages configured.");
			return -1;
		}

		// ✅ Finding the stage transition that matches the given action
		Optional<StageTransition> transitionOpt = currentStage.getNextStages().stream()
				.filter(transition -> transition.getAction().equalsIgnoreCase(action)).findFirst();

		if (transitionOpt.isPresent() && transitionOpt.get().getTargetStageIds() != null
				&& !transitionOpt.get().getTargetStageIds().isEmpty()) {
			Integer targetStageId = transitionOpt.get().getTargetStageIds().get(0);
			System.out.println("✅ Custom Action [" + action + "] → Moving to Stage ID: " + targetStageId);
			return targetStageId;
		}

		System.out.println("⚠️ No matching next stage found for action: " + action);
		return -1;
	}

	private void handleTimeoutScenario(WorkflowTransaction workflowTransaction, Stage currentStage) {
		if (currentStage.getTimeoutConfig() == null || !currentStage.getTimeoutConfig().isTimeoutEnabled()) {
			return; // No timeout handling needed
		}

		int timeoutSeconds = currentStage.getTimeoutConfig().getTimeoutSeconds();
		int fallbackStageId = currentStage.getTimeoutConfig().getFallbackStageId();

		if (isStageTimedOut(workflowTransaction.getUpdatedAt(), timeoutSeconds)) {
			System.out.println("⚠️ Stage Timeout! Moving to Fallback: " + fallbackStageId);
			workflowTransaction.setCurrentStage(String.valueOf(fallbackStageId));
			workflowTransactionRepository.save(workflowTransaction);
			saveWorkflowTransactionHistory(workflowTransaction.getTransactionId(), workflowTransaction,
					currentStage.getStageId(), fallbackStageId, "TIMEOUT");
		}
	}

	private void saveOrUpdatePageData(String transactionId, int stageId, Map<String, Object> pageData) {
		Optional<PageData> existingPageDataOpt = pageDataRepository.findByTransactionIdAndStageId(transactionId,
				String.valueOf(stageId));

		if (existingPageDataOpt.isPresent()) {
			PageData existingPageData = existingPageDataOpt.get();
			existingPageData.setPageData(pageData);
			pageDataRepository.save(existingPageData);
			System.out.println("📝 Page Data Updated for Stage: " + stageId);
		} else {
			PageData newPageData = new PageData(transactionId, String.valueOf(stageId), pageData);
			pageDataRepository.save(newPageData);
			System.out.println("✅ New Page Data Saved for Stage: " + stageId);
		}
	}

	private boolean isStageTimedOut(Instant lastUpdated, int timeoutSeconds) {
		return lastUpdated.plusSeconds(timeoutSeconds).isBefore(Instant.now());
	}

	private void updateWorkflowTransaction(WorkflowTransaction workflowTransaction,
			StageTransaction nextStageTransaction) {
		workflowTransaction.setCurrentStage(String.valueOf(nextStageTransaction.getStageId()));
		workflowTransactionRepository.save(workflowTransaction);
		System.out.println("✅ Transitioned to: " + nextStageTransaction.getStageId());
	}

	private void saveWorkflowTransactionHistory(String transactionId, WorkflowTransaction workflowTransaction,
			int fromStageId, int toStageId, String action) {
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("actor", "System");
		metadata.put("timestamp", Instant.now());

		WorkflowTransactionHistory historyEntry = new WorkflowTransactionHistory(transactionId,
				workflowTransaction.getAppId(), workflowTransaction.getWorkflowId(),
				workflowTransaction.getWorkflowId(), String.valueOf(fromStageId), String.valueOf(toStageId), action,
				"SUCCESS", metadata);
		historyRepository.save(historyEntry);
	}

	/**
	 * Handles rollback to the previous stage based on rollback configuration.
	 */
	private String handleRollback(WorkflowTransaction workflowTransaction, StageTransaction currentStageTransaction,
			String transactionId) {
		System.out.println("🔄 Handling Rollback for Transaction: " + transactionId);

		Integer rollbackStageId = getStageFromList(currentStageTransaction.getRollbackStages(), "ROLLBACK");

		if (rollbackStageId == -1) {
			System.out.println("⚠️ No rollback stage found for stage: " + currentStageTransaction.getStageId());
			return "No rollback stage found!";
		}

		System.out.println("✅ Rolling back to stage: " + rollbackStageId);

		workflowTransaction.setCurrentStage(String.valueOf(rollbackStageId));
		workflowTransactionRepository.save(workflowTransaction);

		saveWorkflowTransactionHistory(transactionId, workflowTransaction, currentStageTransaction.getStageId(),
				rollbackStageId, "ROLLBACK");

		return "Transaction Rolled Back to Stage: " + rollbackStageId;
	}

	/**
	 * Handles jump to a specific stage if allowed.
	 */
	private String handleJump(WorkflowTransaction workflowTransaction, StageTransaction currentStageTransaction,
			String transactionId, int toStageId) {
		System.out.println("🚀 Handling Jump for Transaction: " + transactionId);

		List<StageTransition> jumpStages = currentStageTransaction.getJumpStages();

		if (jumpStages == null || jumpStages.isEmpty()) {
			System.out.println("⚠️ No jump stages configured for stage: " + currentStageTransaction.getStageId());
			return "Jump action is not allowed!";
		}

		boolean isAllowedJump = jumpStages.stream().flatMap(transition -> transition.getTargetStageIds().stream())
				.anyMatch(stageId -> stageId == toStageId);

		if (!isAllowedJump) {
			System.out.println("❌ JUMP action is NOT allowed to stage: " + toStageId);
			return "JUMP action is NOT allowed to stage: " + toStageId;
		}

		System.out.println("✅ Jumping to stage: " + toStageId);

		workflowTransaction.setCurrentStage(String.valueOf(toStageId));
		workflowTransactionRepository.save(workflowTransaction);

		saveWorkflowTransactionHistory(transactionId, workflowTransaction, currentStageTransaction.getStageId(),
				toStageId, "JUMP");

		return "Transaction Jumped to Stage: " + toStageId;
	}

}

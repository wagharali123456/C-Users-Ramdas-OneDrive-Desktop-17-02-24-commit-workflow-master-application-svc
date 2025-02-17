package com.org.fms.mongo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.org.fms.mongo.model.Application;
import com.org.fms.mongo.model.PageData;
import com.org.fms.mongo.model.Stage;
import com.org.fms.mongo.model.StageTransaction;
import com.org.fms.mongo.model.Workflow;
import com.org.fms.mongo.model.WorkflowStageMapping;
import com.org.fms.mongo.model.WorkflowTransaction;
import com.org.fms.mongo.model.WorkflowTransactionHistory;
import com.org.fms.mongo.repository.ApplicationRepository;
import com.org.fms.mongo.repository.PageDataRepository;
import com.org.fms.mongo.repository.StageRepository;
import com.org.fms.mongo.repository.StageTransactionRepository;
import com.org.fms.mongo.repository.WorkflowRepository;
import com.org.fms.mongo.repository.WorkflowStageMappingRepository;
import com.org.fms.mongo.repository.WorkflowTransactionHistoryRepository;
import com.org.fms.mongo.repository.WorkflowTransactionRepository;

@Service
public class WorkflowService {

    private final ApplicationRepository applicationRepository;
    private final WorkflowRepository workflowRepository;
    private final StageRepository stageRepository;
    private final StageTransactionRepository stageTransactionRepository;
    private final WorkflowTransactionHistoryRepository workflowTransactionHistoryRepository;
    private final WorkflowStageMappingRepository workflowStageMappingRepository;
    private final WorkflowTransactionRepository workflowTransactionRepository;
    private final PageDataRepository pageDataRepository;

    public WorkflowService(
    		ApplicationRepository applicationRepository,
            WorkflowRepository workflowRepository,
            StageRepository stageRepository,
            StageTransactionRepository stageTransactionRepository,
            WorkflowStageMappingRepository workflowStageMappingRepository,
            WorkflowTransactionRepository workflowTransactionRepository,
            WorkflowTransactionHistoryRepository workflowTransactionHistoryRepository,
            PageDataRepository pageDataRepository) {
        this.applicationRepository = applicationRepository;
        this.workflowRepository = workflowRepository;
        this.stageRepository = stageRepository;
        this.stageTransactionRepository = stageTransactionRepository;
        this.workflowStageMappingRepository = workflowStageMappingRepository;
        this.workflowTransactionRepository = workflowTransactionRepository;
        this.workflowTransactionHistoryRepository = workflowTransactionHistoryRepository;
        this.pageDataRepository=pageDataRepository;
    }

    /**
     * ✅ Fetch Workflow Details including stages & stage transactions.
     */
    public Optional<Map<String, Object>> getWorkflowWithApplicationAndWorkflow(String appId, String workflowId) {
        Optional<Application> appWorkflowOpt = applicationRepository.findByAppId(appId);
        if (appWorkflowOpt.isPresent()) {
            Application app = appWorkflowOpt.get();
            if (!app.getConnectedWorkflows().contains(workflowId)) {
                return Optional.empty();
            }

            Optional<Workflow> workflowOpt = workflowRepository.findByWorkflowId(workflowId);
            if (workflowOpt.isPresent()) {
                Workflow workflow = workflowOpt.get();
                Map<String, Object> workflowDetails = new HashMap<>();
                workflowDetails.put("appId", app.getAppId());
                workflowDetails.put("appCode", app.getAppCode());
                workflowDetails.put("workflowId", workflow.getWorkflowId());
                workflowDetails.put("workflowName", workflow.getWorkflowName());
                workflowDetails.put("description", workflow.getDescription());
                workflowDetails.put("saveStagePageData", workflow.isSaveStagePageData());

                // ✅ Fetch Workflow Stages from MongoDB
                List<Integer> workflowStages = getWorkflowStagesByWorkflowId(workflow.getWorkflowId());
                List<Stage> stages = stageRepository.findByStageIdIn(workflowStages);

                List<Map<String, Object>> stageDetailsList = new ArrayList<>();
                for (Stage stage : stages) {
                    Map<String, Object> stageData = new HashMap<>();
                    stageData.put("stageId", stage.getStageId());
                    stageData.put("stageName", stage.getName());
                    stageData.put("timeoutConfig", stage.getTimeoutConfig());

                    // Fetch stage transactions for the stage
                    Optional<StageTransaction> stageTransactions = stageTransactionRepository.findByStageId(stage.getStageId());
                    stageData.put("stageTransactions", stageTransactions);

                    stageDetailsList.add(stageData);
                }

                workflowDetails.put("stages", stageDetailsList);
                return Optional.of(workflowDetails);
            }
        }
        return Optional.empty();
    }
    
    /**
     * ✅ Fetch Workflow Details including stages & stage transactions for a specific application
     */
    public Optional<Map<String, Object>> getWorkflowWithApplication(String appId) {
    	 Optional<Application> appOpt = applicationRepository.findByAppId(appId);
        if (appOpt.isEmpty()) {
            return Optional.empty();
        }

        Application app = appOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("appId", app.getAppId());
        response.put("appCode", app.getAppCode());
        response.put("workflowIds", app.getConnectedWorkflows());

        List<Map<String, Object>> workflowsWithStages = new ArrayList<>();

        for (String workflowId : app.getConnectedWorkflows()) {
        	  Optional<Workflow> workflowOpt = workflowRepository.findByWorkflowId(workflowId);
            if (workflowOpt.isPresent()) {
                Workflow workflow = workflowOpt.get();
                Map<String, Object> workflowData = new HashMap<>();
                workflowData.put("workflowId", workflow.getWorkflowId());
                workflowData.put("workflowName", workflow.getWorkflowName());
                workflowData.put("description", workflow.getDescription());
                workflowData.put("saveStagePageData", workflow.isSaveStagePageData());

                // ✅ Fetch Workflow Stages from MongoDB
                List<Integer> workflowStages = getWorkflowStagesByWorkflowId(workflow.getWorkflowId());
                List<Stage> stages = stageRepository.findByStageIdIn(workflowStages);

                List<Map<String, Object>> stageDetailsList = new ArrayList<>();
                for (Stage stage : stages) {
                    Map<String, Object> stageData = new HashMap<>();
                    stageData.put("stageId", stage.getStageId());
                    stageData.put("stageName", stage.getName());
                    stageData.put("timeoutConfig", stage.getTimeoutConfig());

                    // Fetch stage transactions for the stage
                    Optional<StageTransaction> stageTransactions = stageTransactionRepository.findByStageId(stage.getStageId());
                    stageData.put("stageTransactions", stageTransactions);

                    stageDetailsList.add(stageData);
                }

                workflowData.put("stages", stageDetailsList);
                workflowsWithStages.add(workflowData);
            }
        }

        response.put("workflows", workflowsWithStages);
        return Optional.of(response);
    }
    
    /**
     * ✅ Fetch **All Applications** with their **Workflows**, **Stages**, and **Stage Transactions**
     */
    public List<Map<String, Object>> getAllWorkflowAndApplication() {
   	 List<Application> appList = applicationRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Application appWorkflow : appList) {
            Map<String, Object> response = new HashMap<>();
            response.put("appId", appWorkflow.getAppId());
            response.put("appCode", appWorkflow.getAppCode());
            response.put("workflowIds", appWorkflow.getConnectedWorkflows());

            List<Map<String, Object>> workflowsWithStages = new ArrayList<>();

            for (String workflowId : appWorkflow.getConnectedWorkflows()) {
            	System.out.println("workflowId : "+workflowId);
                Optional<Workflow> workflowOpt = workflowRepository.findByWorkflowId(workflowId);
            	System.out.println("workflowOpt : "+workflowOpt);
                if (!workflowOpt.isEmpty()) {
                	System.out.println("workflowOpt : "+workflowOpt);
                    Workflow workflow = workflowOpt.get();
                    Map<String, Object> workflowData = new HashMap<>();
                    workflowData.put("workflowId", workflow.getWorkflowId());
                    workflowData.put("workflowName", workflow.getWorkflowName());
                    workflowData.put("description", workflow.getDescription());
                    workflowData.put("saveStagePageData", workflow.isSaveStagePageData());

                    // ✅ Fetch Workflow Stages from MongoDB
                    List<Integer> workflowStages = getWorkflowStagesByWorkflowId(workflow.getWorkflowId());
                    List<Stage> stages = stageRepository.findByStageIdIn(workflowStages);

                    List<Map<String, Object>> stageDetailsList = new ArrayList<>();
                    for (Stage stage : stages) {
                        Map<String, Object> stageData = new HashMap<>();
                        stageData.put("stageId", stage.getStageId());
                        stageData.put("stageName", stage.getName());
                        stageData.put("timeoutConfig", stage.getTimeoutConfig());

                        // Fetch stage transactions for the stage
                        Optional<StageTransaction> stageTransactions = stageTransactionRepository.findByStageId(stage.getStageId());
                        stageData.put("stageTransactions", stageTransactions);

                        stageDetailsList.add(stageData);
                    }

                    workflowData.put("stages", stageDetailsList);
                    workflowsWithStages.add(workflowData);
                }
            }

            response.put("workflows", workflowsWithStages);
            result.add(response);
        }
        return result;
    }

    /**
     * ✅ Fetch Workflow Stages from MongoDB using WorkflowStageMappingRepository
     */
    private List<Integer> getWorkflowStagesByWorkflowId(String workflowId) {
        List<Integer> stageIds = new ArrayList<>();
        
        List<WorkflowStageMapping> mappings = workflowStageMappingRepository.findByWorkflowId(workflowId);
        for (WorkflowStageMapping mapping : mappings) {
            stageIds.addAll(mapping.getStages());
        }

        return stageIds;
    }
    

    /**
     * ✅ Fetch Current Stage by transactionId & workflowId
     */
    public Optional<Map<String, Object>> getWorkflowCurrentStage(String transactionId, String workflowId) {
        System.out.println("transactionId : " + transactionId);
        System.out.println("workflowId : " + workflowId);

        Optional<WorkflowTransaction> workflowTransactionOpt = workflowTransactionRepository.findByTransactionIdAndWorkflowId(transactionId, workflowId);
        if (!workflowTransactionOpt.isPresent()) {
            return Optional.empty();
        }

        WorkflowTransaction workflowTransaction = workflowTransactionOpt.get();
        String currentStageId = workflowTransaction.getCurrentStage();

        // ✅ Fetch StageTransaction for the current stage
        Optional<StageTransaction> stageTransactionOpt = stageTransactionRepository.findByTransactionIdAndStageId(transactionId, Integer.parseInt(currentStageId));

        // ✅ Fetch StageTransactionHistory to get previous transitions
        List<WorkflowTransactionHistory> historyList = workflowTransactionHistoryRepository.findByTransactionId(transactionId);
        
        // ✅ Fetch Stage Page Data
        Optional<PageData> pageDataOpt = pageDataRepository.findByTransactionIdAndStageId(transactionId, currentStageId);


        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", transactionId);
        response.put("workflowId", workflowId);
        response.put("appId", workflowTransaction.getAppId());
        response.put("currentStageId", currentStageId);
        response.put("status", workflowTransaction.getStatus());

        if (stageTransactionOpt.isPresent()) {
            StageTransaction stageTransaction = stageTransactionOpt.get();
            response.put("currentStageName", stageTransaction.getStageName());
            response.put("action", stageTransaction.getAction());
            response.put("allowedActions", stageTransaction.getAllowedActions());
            response.put("notAllowedActions", stageTransaction.getNotAllowedActions());
        } else {
            response.put("currentStageName", "N/A");
            response.put("action", "N/A");
            response.put("allowedActions", List.of());
            response.put("notAllowedActions", List.of());
        }

        response.put("metadata", workflowTransaction.getMetadata());
        
        // ✅ Add history details
        List<Map<String, Object>> historyRecords = new ArrayList<>();
        for (WorkflowTransactionHistory history : historyList) {
            Map<String, Object> historyData = new HashMap<>();
            historyData.put("fromStage", history.getFromStage());
            historyData.put("toStage", history.getToStage());
            historyData.put("action", history.getAction());
            historyData.put("timestamp", history.getMetadata().get("timestamp"));
            historyRecords.add(historyData);
        }
        response.put("history", historyRecords);
        

        // ✅ Add Page Data if available
        if (pageDataOpt.isPresent()) {
            PageData pageData = pageDataOpt.get();
            Map<String, Object> pageDataDetails = new HashMap<>();
            pageDataDetails.put("pageData", pageData.getPageData());
            pageDataDetails.put("isReopened", pageData.isReopened());
            pageDataDetails.put("isTimedOut", pageData.isTimedOut());
            pageDataDetails.put("fallbackStageId", pageData.getFallbackStageId());
            pageDataDetails.put("createdAt", pageData.getCreatedAt());
            pageDataDetails.put("updatedAt", pageData.getUpdatedAt());

            response.put("stagePageData", pageDataDetails);
        } else {
            response.put("stagePageData", "No data available");
        }

        return Optional.of(response);
    }

}

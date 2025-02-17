package com.org.fms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.fms.mongo.service.WorkflowService;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

	private final WorkflowService workflowService;

	public WorkflowController(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	// ✅ Fetch all workflows with application and stage details
	@GetMapping("/all")
	public ResponseEntity<List<Map<String, Object>>> getAllWorkflowAndApplication() {
		return ResponseEntity.ok(workflowService.getAllWorkflowAndApplication());
	}

	// ✅ Fetch all workflows with application and stage details
	@GetMapping("/{appId}")
	public ResponseEntity<Optional<Map<String, Object>>> getWorkflowWithApplication(@PathVariable String appId) {
		return ResponseEntity.ok(workflowService.getWorkflowWithApplication(appId));
	}

	// ✅ Fetch a specific workflow with application and stage details
	@GetMapping("/{appId}/{workflowId}")
	public ResponseEntity<Optional<Map<String, Object>>> getWorkflowWithApplicationandWorkflow(
			@PathVariable String appId, @PathVariable String workflowId) {
		return ResponseEntity.ok(workflowService.getWorkflowWithApplicationAndWorkflow(appId, workflowId));
	}
	
	@GetMapping("/current-stage/{transactionId}/{workflowId}")
	public ResponseEntity<Map<String, Object>> getWorkflowCurrentStage(
	        @PathVariable String transactionId, @PathVariable String workflowId) {
	    
	    Optional<Map<String, Object>> result = workflowService.getWorkflowCurrentStage(transactionId, workflowId);

	    if (result.isPresent()) {
	        return ResponseEntity.ok(result.get());
	    } else {
	        // Return 404 with a meaningful message
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("message", "No WorkflowTransaction found for transactionId: " + transactionId + " and workflowId: " + workflowId);
	        return ResponseEntity.status(404).body(errorResponse);
	    }
	}
}

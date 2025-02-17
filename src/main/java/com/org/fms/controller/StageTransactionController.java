package com.org.fms.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.fms.model.StageTransitionRequest;
import com.org.fms.mongo.service.StageTransactionService;

@RestController
@RequestMapping("/api/stage-transactions")
public class StageTransactionController {
	private final StageTransactionService stageTransactionService;

	public StageTransactionController(StageTransactionService stageTransactionService) {
		this.stageTransactionService = stageTransactionService;
	}

	@PostMapping(value = "/transition", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Map<String, Object>> processTransition(@RequestBody StageTransitionRequest request) {
		Map<String, Object> response = stageTransactionService.processStageTransition(request);
		return ResponseEntity.ok(response);
	} 

	/*
	 * { "transactionId": "TXN123456", "action": "JUMP", "pageData": { "field1":
	 * "value1", "field2": "value2" }, "targetStageName": "Proposal Approval" }
	 */

} 

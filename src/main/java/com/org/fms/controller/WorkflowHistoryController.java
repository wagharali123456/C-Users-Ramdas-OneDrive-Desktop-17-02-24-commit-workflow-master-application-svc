package com.org.fms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.fms.mongo.model.WorkflowTransactionHistory;
import com.org.fms.mongo.repository.WorkflowTransactionHistoryRepository;

@RestController
@RequestMapping("/api/workflow-history")
public class WorkflowHistoryController {
    private final WorkflowTransactionHistoryRepository historyRepository;

    public WorkflowHistoryController(WorkflowTransactionHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<List<WorkflowTransactionHistory>> getTransactionHistory(@PathVariable String transactionId) {
        List<WorkflowTransactionHistory> history = historyRepository.findByTransactionIdOrderByTimestampAsc(transactionId);
        return ResponseEntity.ok(history);
    }
}

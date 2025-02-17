package com.org.fms.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.fms.mongo.model.StartWorkflowRequest;
import com.org.fms.mongo.service.StartWorkflowService;

@RestController
@RequestMapping("/api/workflows")
public class StartCloseWorkflowController {
    private final StartWorkflowService startWorkflowService;

    public StartCloseWorkflowController(StartWorkflowService startWorkflowService) {
        this.startWorkflowService = startWorkflowService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startWorkflow(@RequestBody StartWorkflowRequest request) {
        Map<String, Object> response = startWorkflowService.startWorkflow(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/close")
    public ResponseEntity<Map<String, Object>> closeWorkflow(@RequestBody StartWorkflowRequest request) {
        Map<String, Object> response = startWorkflowService.closeWorkflow(request);
        return ResponseEntity.ok(response);
    }
}

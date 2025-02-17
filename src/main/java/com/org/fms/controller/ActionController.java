//package com.org.fms.controller;
//
//import org.springframework.web.bind.annotation.*;
//import com.org.fms.mongo.service.ActionService;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/actions")
//public class ActionController {
//
//    private final ActionService actionService;
//
//    public ActionController(ActionService actionService) {
//        this.actionService = actionService;
//    }
//
//    @GetMapping("/allowed")
//    public List<String> getAllowedActions(@RequestParam String appId, 
//                                          @RequestParam String workflowId, 
//                                          @RequestParam String stageId) {
//        return actionService.getAllowedActions(appId, workflowId, stageId);
//    }
//
//    @GetMapping("/not-allowed")
//    public List<String> getNotAllowedActions(@RequestParam String appId, 
//                                             @RequestParam String workflowId, 
//                                             @RequestParam String stageId) {
//        return actionService.getNotAllowedActions(appId, workflowId, stageId);
//    }
//}

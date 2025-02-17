package com.org.fms.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import com.org.fms.mongo.service.GenericActionService;
import com.org.fms.mongo.model.GenericAction;

@RestController
@RequestMapping("/api/system-actions")
public class GenericActionController {

    private final GenericActionService genericActionService;

    public GenericActionController(GenericActionService genericActionService) {
        this.genericActionService = genericActionService;
    }

    @GetMapping("/{actionCode}")
    public Optional<GenericAction> getActionByCode(@PathVariable String actionCode) {
        return genericActionService.getActionByCode(actionCode);
    }
}

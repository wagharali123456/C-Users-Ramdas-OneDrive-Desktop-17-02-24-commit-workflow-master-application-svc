package com.org.fms.mongo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.org.fms.mongo.model.GenericAction;
import com.org.fms.mongo.repository.GenericActionRepository;

@Service
public class GenericActionService {

    private final GenericActionRepository genericActionRepository;

    public GenericActionService(GenericActionRepository genericActionRepository) {
        this.genericActionRepository = genericActionRepository;
    }

    public Optional<GenericAction> getActionByCode(String actionCode) {
        return genericActionRepository.findByActionCode(actionCode);
    }
}

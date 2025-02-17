package com.org.fms.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.org.fms.mongo.model.GenericAction;

public interface GenericActionRepository extends MongoRepository<GenericAction, String> {
    Optional<GenericAction> findByActionCode(String actionCode);
}

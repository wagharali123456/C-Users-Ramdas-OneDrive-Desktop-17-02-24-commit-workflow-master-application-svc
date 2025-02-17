package com.org.fms.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.org.fms.mongo.model.WorkflowTransaction;

@Repository
public interface WorkflowTransactionRepository extends MongoRepository<WorkflowTransaction, String> {
    Optional<WorkflowTransaction> findByTransactionId(String transactionId);
    Optional<WorkflowTransaction> findByTransactionIdAndWorkflowId(String transactionId, String workflowId);
	Optional<WorkflowTransaction> findByTransactionIdAndAppIdAndWorkflowId(String transactionId,String appId,String workflowId);
}

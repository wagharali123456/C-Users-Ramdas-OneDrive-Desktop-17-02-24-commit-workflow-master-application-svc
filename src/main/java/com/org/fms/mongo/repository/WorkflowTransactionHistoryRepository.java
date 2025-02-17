package com.org.fms.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.org.fms.mongo.model.WorkflowTransactionHistory;

@Repository
public interface WorkflowTransactionHistoryRepository extends MongoRepository<WorkflowTransactionHistory, String> {
    List<WorkflowTransactionHistory> findByTransactionIdOrderByTimestampAsc(String transactionId);

    List<WorkflowTransactionHistory> findByTransactionIdAndToStage(String transactionId, String toStage);

	List<WorkflowTransactionHistory> findByTransactionId(String transactionId);
}

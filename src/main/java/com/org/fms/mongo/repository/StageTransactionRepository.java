package com.org.fms.mongo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.org.fms.mongo.model.StageTransaction;

@Repository
public interface StageTransactionRepository extends MongoRepository<StageTransaction, String> {
    Optional<StageTransaction> findByStageName(String stageName);

	Optional<StageTransaction> findByStageId(Integer firstStageId);

	Optional<StageTransaction> findByTransactionIdAndStageId(String transactionId, int currentStageId);

	List<StageTransaction> findByTransactionId(String transactionId);
}

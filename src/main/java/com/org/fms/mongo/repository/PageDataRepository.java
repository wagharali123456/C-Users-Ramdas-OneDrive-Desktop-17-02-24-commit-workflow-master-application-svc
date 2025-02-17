package com.org.fms.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.org.fms.mongo.model.PageData;
import java.util.Optional;

public interface PageDataRepository extends MongoRepository<PageData, String> {
    Optional<PageData> findByTransactionIdAndStageId(String transactionId, String stageId);
}

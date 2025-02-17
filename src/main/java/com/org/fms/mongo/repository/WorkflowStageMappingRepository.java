package com.org.fms.mongo.repository;

import com.org.fms.mongo.model.WorkflowStageMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WorkflowStageMappingRepository extends MongoRepository<WorkflowStageMapping, String> {
    List<WorkflowStageMapping> findByWorkflowId(String workflowId);
}

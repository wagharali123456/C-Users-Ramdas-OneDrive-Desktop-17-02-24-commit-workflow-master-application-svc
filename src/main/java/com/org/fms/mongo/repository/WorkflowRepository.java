package com.org.fms.mongo.repository;

import com.org.fms.mongo.model.Workflow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface WorkflowRepository extends MongoRepository<Workflow, String> {

    Optional<Workflow> findByWorkflowName(String name);

	List<Workflow> findByWorkflowNameIn(List<String> workflowNames);

	Optional<Workflow> findByWorkflowId(String workflowId);

 
}

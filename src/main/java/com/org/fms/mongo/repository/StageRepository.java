package com.org.fms.mongo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.org.fms.mongo.model.Stage;

public interface StageRepository extends MongoRepository<Stage, String> {
	Optional<Stage> findByName(String name);

	Optional<Stage> findByStageId(int currentStageId);
	
    // Fetch stages by workflow ID using the mapped workflowId field
    List<Stage> findByStageIdIn(List<Integer> stageIds);
}

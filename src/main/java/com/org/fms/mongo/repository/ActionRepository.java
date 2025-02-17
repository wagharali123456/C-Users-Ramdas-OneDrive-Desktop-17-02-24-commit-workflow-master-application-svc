//package com.org.fms.mongo.repository;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//import com.org.fms.mongo.model.Action;
//import java.util.List;
//import java.util.Optional;
//
//public interface ActionRepository extends MongoRepository<Action, String> {
//    List<Action> findByAppId(String appId);
//    List<Action> findByWorkflowId(String workflowId);
//    List<Action> findByStageId(String stageId);
//	Optional<Action> findByAppIdAndWorkflowIdAndStageId(String appId, String workflowId, String stageId);
//	Optional<Action> findByActionCode(String action);
//}

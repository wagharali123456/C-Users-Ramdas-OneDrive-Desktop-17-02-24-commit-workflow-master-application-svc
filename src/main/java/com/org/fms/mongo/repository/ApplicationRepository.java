package com.org.fms.mongo.repository;

import com.org.fms.mongo.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

    /**
     * Find an application by its application code.
     *
     * @param appCode The unique application code.
     * @return Optional<Application> if found.
     */
    Optional<Application> findByAppCode(String appCode);

    /**
     * Find an application by its name.
     *
     * @param name The application name.
     * @return Optional<Application> if found.
     */
    Optional<Application> findByName(String name);

	Optional<Application> findByAppId(String appId);
}

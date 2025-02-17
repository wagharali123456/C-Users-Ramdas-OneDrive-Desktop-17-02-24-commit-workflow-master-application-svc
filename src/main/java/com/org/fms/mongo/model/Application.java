package com.org.fms.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Document(collection = "applications")
public class Application {

    @Id
    private String id;

    @Field("appCode")
    private String appCode;

    @Field("appId")
    private String appId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("relatedApplications")
    private List<String> relatedApplications;

    @Field("connectedWorkflows")
    private List<String> connectedWorkflows;

    public Application() {}

    public Application(String appCode, String appId, String name, String description,
                       List<String> relatedApplications, List<String> connectedWorkflows) {
        this.appCode = appCode;
        this.appId = appId;
        this.name = name;
        this.description = description;
        this.relatedApplications = relatedApplications;
        this.connectedWorkflows = connectedWorkflows;
    }

    public String getId() { return id; }
    public String getAppCode() { return appCode; }
    public String getAppId() { return appId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getRelatedApplications() { return relatedApplications; }
    public List<String> getConnectedWorkflows() { return connectedWorkflows; }

    public void setRelatedApplications(List<String> relatedApplications) {
        this.relatedApplications = relatedApplications;
    }

    public void setConnectedWorkflows(List<String> connectedWorkflows) {
        this.connectedWorkflows = connectedWorkflows;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", appCode='" + appCode + '\'' +
                ", appId='" + appId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", relatedApplications=" + relatedApplications +
                ", connectedWorkflows=" + connectedWorkflows +
                '}';
    }
}

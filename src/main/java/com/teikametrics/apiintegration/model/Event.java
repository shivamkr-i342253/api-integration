package com.teikametrics.apiintegration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Event {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("actor")
    private Map<String, String> actor;

    @JsonProperty("repo")
    private Map<String, String> repo;

    @JsonProperty("payload")
    private Map<String, Object> payload;

    @JsonProperty("public")
    private boolean isPublic;

    @JsonProperty("created_at")
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getActor() {
        return actor;
    }

    public void setActor(Map<String, String> actor) {
        this.actor = actor;
    }

    public Map<String, String> getRepo() {
        return repo;
    }

    public void setRepo(Map<String, String> repo) {
        this.repo = repo;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

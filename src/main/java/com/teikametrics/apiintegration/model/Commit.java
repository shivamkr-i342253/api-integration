package com.teikametrics.apiintegration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Commit {

    @JsonProperty("sha")
    private String sha;

    @JsonProperty("author")
    private Map<String, String> author;

    @JsonProperty("message")
    private String message;

    @JsonProperty("distinct")
    private boolean distinct;

    @JsonProperty("url")
    private String url;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Map<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, String> author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

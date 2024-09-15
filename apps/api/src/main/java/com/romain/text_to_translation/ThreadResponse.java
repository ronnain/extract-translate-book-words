package com.romain.text_to_translation;

import java.util.Map;

public class ThreadResponse {

    private String id;
    private String object;
    private long created_at;
    private Map<String, Object> metadata;
    private Map<String, Object> tool_resources;

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(long created_at) {
        this.created_at = created_at;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getToolResources() {
        return tool_resources;
    }

    public void setToolResources(Map<String, Object> tool_resources) {

    }
}
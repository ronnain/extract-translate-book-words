package com.romain.text_to_translation;

public class ApiResponse {

    private String id;
    private String status;
    private String assistant_id;
    private String thread_id;
    private Long created_at;
    private Long started_at;
    private Long completed_at;
    private Long failed_at;

    // Getters et setters pour chaque champ

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssistantId() {
        return assistant_id;
    }

    public void setAssistantId(String assistant_id) {
        this.assistant_id = assistant_id;
    }

    public String getThreadId() {
        return thread_id;
    }

    public void setThreadId(String thread_id) {
        this.thread_id = thread_id;
    }

    public Long getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Long created_at) {
        this.created_at = created_at;
    }

    public Long getStartedAt() {
        return started_at;
    }

    public void setStartedAt(Long started_at) {
        this.started_at = started_at;
    }

    public Long getCompletedAt() {
        return completed_at;
    }

    public void setCompletedAt(Long completed_at) {
        this.completed_at = completed_at;
    }

    public Long getFailedAt() {
        return failed_at;
    }

    public void setFailedAt(Long failed_at) {
        this.failed_at = failed_at;
    }
}

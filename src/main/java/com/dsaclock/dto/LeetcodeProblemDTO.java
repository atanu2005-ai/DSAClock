package com.dsaclock.dto;

public class LeetcodeProblemDTO {

    private Long frontend_id;

    private String title;

    private String description;

    private String difficulty;

    private String url;

    public Long getFrontend_id() {
        return frontend_id;
    }

    public void setFrontend_id(Long frontend_id) {
        this.frontend_id = frontend_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.dsaclock.dto;

public class LeetcodeProblemDTO {

    private Long frontend_id;

    private String title;

    private String description;

    private String difficulty;

    private String url;

    private String title_slug;

    private Integer likes;

    private Integer dislikes;

    public Long getFrontend_id() {
        return frontend_id;
    }

    public void setFrontend_id(Long frontend_id) {
        this.frontend_id = frontend_id;
    }

    public String getTitle_slug() {
        return title_slug;
    }

    public void setTitle_slug(String title_slug) {
        this.title_slug = title_slug;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
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

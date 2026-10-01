package com.dsaclock.dto;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {

    private String username;

    private String about;

    private String leetcodeProfileUrl;

    private String githubProfileUrl;

    private String linkedinProfileUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLeetcodeProfileUrl() {
        return leetcodeProfileUrl;
    }

    public void setLeetcodeProfileUrl(String leetcodeProfileUrl) {
        this.leetcodeProfileUrl = leetcodeProfileUrl;
    }

    public String getGithubProfileUrl() {
        return githubProfileUrl;
    }

    public void setGithubProfileUrl(String githubProfileUrl) {
        this.githubProfileUrl = githubProfileUrl;
    }

    public String getLinkedinProfileUrl() {
        return linkedinProfileUrl;
    }

    public void setLinkedinProfileUrl(String linkedinProfileUrl) {
        this.linkedinProfileUrl = linkedinProfileUrl;
    }

    public String getTwitterProfileUrl() {
        return twitterProfileUrl;
    }

    public void setTwitterProfileUrl(String twitterProfileUrl) {
        this.twitterProfileUrl = twitterProfileUrl;
    }

    private String twitterProfileUrl;
}

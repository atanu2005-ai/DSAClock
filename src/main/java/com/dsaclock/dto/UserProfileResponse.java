package com.dsaclock.dto;

public class UserProfileResponse {

    private Long id;

    private String username;

    private String email;

    private String about;

    private String leetcodeProfileUrl;

    private String githubProfileUrl;

    private String linkedinProfileUrl;

    private String twitterProfileUrl;

    private int totalProblemsSolved;

    private int totalRevisions;

    private int revised;

    private int revisedAtLeastTwice;

    private double revisedPercentage;

    private double revisedTwicePercentage;

    private int currentStreak;

    private int maxStreak;

    public Long getId() {
        return id;
    }

    public int getRevised() {
        return revised;
    }

    public double getRevisedPercentage() {
        return revisedPercentage;
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

    public double getRevisedTwicePercentage() {
        return revisedTwicePercentage;
    }

    public void setRevisedTwicePercentage(double revisedTwicePercentage) {
        this.revisedTwicePercentage = revisedTwicePercentage;
    }

    public int getRevisedAtLeastTwice() {
        return revisedAtLeastTwice;
    }

    public void setRevisedAtLeastTwice(int revisedAtLeastTwice) {
        this.revisedAtLeastTwice = revisedAtLeastTwice;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public void setRevisedPercentage(double revisedPercentage) {
        this.revisedPercentage = revisedPercentage;
    }

    public void setRevised(int revised) {
        this.revised = revised;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(int totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public int getTotalRevisions() {
        return totalRevisions;
    }

    public void setTotalRevisions(int totalRevisions) {
        this.totalRevisions = totalRevisions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

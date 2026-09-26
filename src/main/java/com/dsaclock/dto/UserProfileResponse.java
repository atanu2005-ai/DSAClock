package com.dsaclock.dto;

public class UserProfileResponse {

    private Long id;

    private String username;

    private String email;

    private int totalProblemsSolved;

    private int totalRevisions;

    private int revised;

    private double revisedPercentage;

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

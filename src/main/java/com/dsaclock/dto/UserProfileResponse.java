package com.dsaclock.dto;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private int totalProblemsSolved;
    private int totalRevisions;

    public Long getId() {
        return id;
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

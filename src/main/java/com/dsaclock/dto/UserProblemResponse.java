package com.dsaclock.dto;

import java.time.LocalDate;

public class UserProblemResponse {

    private Long problemId;

    private String title;

    private String difficulty;

    private LocalDate solved_date;

    private LocalDate next_revision_date;

    private int revision_count;

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getSolved_date() {
        return solved_date;
    }

    public void setSolved_date(LocalDate solved_date) {
        this.solved_date = solved_date;
    }

    public LocalDate getNext_revision_date() {
        return next_revision_date;
    }

    public void setNext_revision_date(LocalDate next_revision_date) {
        this.next_revision_date = next_revision_date;
    }

    public int getRevision_count() {
        return revision_count;
    }

    public void setRevision_count(int revision_count) {
        this.revision_count = revision_count;
    }
}

package com.dsaclock.dto;

import java.time.LocalDate;

public class RevisionActivityResponse {

    private LocalDate date;

    private int revisionCount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(int revisionCount) {
        this.revisionCount = revisionCount;
    }
}

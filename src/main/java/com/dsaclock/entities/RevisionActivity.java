package com.dsaclock.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "RevisionActivity", uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "revision_date"})
        }
)
public class RevisionActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revisionId;

    @ManyToOne(fetch = FetchType.LAZY) //tells hibernate not to load user object unless needed
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "revision_date", nullable = false)
    private LocalDate activityRevisionDate;

    @Column(name = "revision_count", nullable = false)
    private int onDateRevisionCount;

    public Long getRevisionId() {
        return revisionId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDate getActivityRevisionDate() {
        return activityRevisionDate;
    }

    public void setActivityRevisionDate(LocalDate activityRevisionDate) {
        this.activityRevisionDate = activityRevisionDate;
    }

    public int getOnDateRevisionCount() {
        return onDateRevisionCount;
    }

    public void setOnDateRevisionCount(int onDateRevisionCount) {
        this.onDateRevisionCount = onDateRevisionCount;
    }
}

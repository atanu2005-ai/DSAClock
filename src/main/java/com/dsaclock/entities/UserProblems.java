package com.dsaclock.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "UserProblems", uniqueConstraints = {  //This is tell hibernate, "look, this column
                                                     //duo should be unique in th user problems"
        @UniqueConstraint(columnNames = {"userId", "problemId"})
})
public class UserProblems {

    //ID of user problem
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProblemId;

    //reference to the user who added this problem
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    ///reference to the problem the user added
    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problems problem;

    //solved date
    private LocalDate solved_date;

    //revision date
    private LocalDate next_revision_date;

    //revision count
    private int revision_count;

    //This constructor defines clearly that this entity has a relation with user and problem objects
    public UserProblems(Users user, Problems problem) {
        this.user = user;
        this.problem = problem;
    }
    //no argument constructor as it will not be created automatically anymore
    public UserProblems() {}

    //getters and setters starts here
    public Long getUserProblemId() {
        return userProblemId;
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

    public Users getUser() {
        return user;
    }

    public LocalDate getSolved_date() {
        return solved_date;
    }

    public void setUserProblemId(Long userProblemId) {
        this.userProblemId = userProblemId;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setProblem(Problems problem) {
        this.problem = problem;
    }

    public Problems getProblem() {
        return problem;
    }

    public void setSolved_date(LocalDate solved_date) {
        this.solved_date = solved_date;
    }

}

package com.dsaclock.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Problems")
public class Problems {
    //problem id
    @Id
    @Column(nullable = false)
    private Long problemId;

    //problem title
    private String problem_title;

    //problem description
    private String problem_desc;

    //problem difficulty
    private String problem_diff;

    //problem url
    private String problem_url;

    //Getters and setters from here
    public Long getProblemId() {
        return problemId;
    }

    public String getProblem_url() {
        return problem_url;
    }

    public void setProblem_url(String problem_url) {
        this.problem_url = problem_url;
    }

    public String getProblem_title() {
        return problem_title;
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public String getProblem_diff() {
        return problem_diff;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public void setProblem_title(String problem_title) {
        this.problem_title = problem_title;
    }

    public void setProblem_desc(String problem_desc) {
        this.problem_desc = problem_desc;
    }

    public void setProblem_diff(String problem_diff) {
        this.problem_diff = problem_diff;
    }

}

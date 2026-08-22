package com.eddy.dsaclockbackend.dsaclock.entities;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDate;

@Entity
@Table(name = "Problems")
public class Problems {
    //problem id
    @Id
    @Column(nullable = false)
    private Long problem_id;

    //problem title
    private String problem_title;

    //problem description
    private String problem_desc;

    //problem difficulty
    private String problem_diff;

    //Getters and setters from here
    public Long getProblem_id() {
        return problem_id;
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

    public void setProblem_id(Long problem_id) {
        this.problem_id = problem_id;
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

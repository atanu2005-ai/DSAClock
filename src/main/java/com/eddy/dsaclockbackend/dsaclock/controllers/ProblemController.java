package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.services.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {
    ProblemService problemService;
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    //get all problems
    @GetMapping
    public List<Problems> getProblems() {
        return problemService.getProblem();
    }

    //get a problem by id
    @GetMapping("/{problem_id}")
    public ResponseEntity<Problems> getProblems(@PathVariable Long problem_id) {
        Optional<Problems> thisProblem = problemService.getProblem(problem_id);

        if(thisProblem.isPresent()) {
            return ResponseEntity.ok(thisProblem.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //add a new problem
    @PostMapping
    public Problems addProblem(@RequestBody Problems problem) {
        problemService.addProblem(problem);
        return problem;
    }

    //update a problem
    @PutMapping("/{problem_id}")
    public ResponseEntity<Problems> updateProblem(
            @PathVariable Long problem_id,
            @RequestBody Problems problem) {

        Optional<Problems> thisProblem = problemService.getProblem(problem_id);

        if (thisProblem.isPresent()) {
            problem.setProblem_id(problem_id);
            problemService.updateProblem(problem);
            return ResponseEntity.ok(problem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a problem
    @DeleteMapping("/{problem_id}")
    public ResponseEntity<Problems> deleteProblem(@PathVariable Long problem_id) {

        Optional<Problems> thisProblem = problemService.getProblem(problem_id);

        if (thisProblem.isPresent()) {
            problemService.deleteProblem(problem_id);
            return ResponseEntity.ok(thisProblem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

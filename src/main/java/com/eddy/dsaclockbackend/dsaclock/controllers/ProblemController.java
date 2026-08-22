package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.services.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{problemId}")
    public ResponseEntity<Problems> getProblems(@PathVariable Long problemId) {
        Optional<Problems> thisProblem = problemService.getProblem(problemId);

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
    @PutMapping("/{problemId}")
    public ResponseEntity<Problems> updateProblem(
            @PathVariable Long problemId,
            @RequestBody Problems problem) {

        Optional<Problems> thisProblem = problemService.getProblem(problemId);

        if (thisProblem.isPresent()) {
            problem.setProblemId(problemId);
            problemService.updateProblem(problem);
            return ResponseEntity.ok(problem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a problem
    @DeleteMapping("/{problemId}")
    public ResponseEntity<Problems> deleteProblem(@PathVariable Long problemId) {

        Optional<Problems> thisProblem = problemService.getProblem(problemId);

        if (thisProblem.isPresent()) {
            problemService.deleteProblem(problemId);
            return ResponseEntity.ok(thisProblem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

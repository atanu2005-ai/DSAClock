package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import com.eddy.dsaclockbackend.dsaclock.services.ProblemService;
import com.eddy.dsaclockbackend.dsaclock.services.UserProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    //problem service reference
    ProblemService problemService;

    //user problem service reference
    UserProblemService userProblemService;

    //user repo reference
    UserRepo userRepo;

    public ProblemController(ProblemService problemService,
                             UserProblemService userProblemService,
                             UserRepo userRepo) {
        this.problemService = problemService;
        this.userProblemService = userProblemService;
        this.userRepo = userRepo;
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

    /*
    * user problem controll
    * ----------------------*/

    //add a problem to a user
    @PostMapping("{problemId}/add")
    public ResponseEntity<UserProblems> addUserProblem(@PathVariable Long problemId,
                                                       @RequestBody UserProblems userProblems) { //for solved date

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication(); //fetching current user's authentication object

        assert auth != null;
        String email = auth.getName(); //fetching email of current user using auth object

        Users user =
                userRepo
                        .findByEmail(email) //create user object with th email
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //this will return empty optional if the unique constraint already exists
        Optional<UserProblems> thisUserProblem =
                userProblemService
                        .addUserProblem(user.getUserId(), problemId, userProblems);

        if(thisUserProblem.isPresent()) {
            return ResponseEntity.ok(thisUserProblem.get());
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //
        }


    }
}

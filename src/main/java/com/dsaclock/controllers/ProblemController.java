package com.dsaclock.controllers;

import com.dsaclock.entities.Problems;
import com.dsaclock.entities.UserProblems;
import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.ProblemService;
import com.dsaclock.services.UserProblemService;
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
    public Problems getProblems(@PathVariable Long problemId) {

        return problemService.getProblem(problemId);
    }

    //add a new problem
    @PostMapping
    public Problems addProblem(@RequestBody Problems problem) {
        problemService.addProblem(problem);
        return problem;
    }

    //update a problem
    @PutMapping("/{problemId}")
    public ResponseEntity<?> updateProblem(
            @PathVariable Long problemId,
            @RequestBody Problems problem) {

        problem.setProblemId(problemId);
        problemService.updateProblem(problem);

        return ResponseEntity.ok(problem);
    }

    //delete a problem
    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteProblem(@PathVariable Long problemId) {

        problemService.deleteProblem(problemId);

        return ResponseEntity.ok().body("Problem deleted successfully!");
    }

    /*
    * user problem controll
    * ----------------------*/

    //add a problem to a user
    @PostMapping("{problemId}/add")
    public UserProblems addUserProblem(@PathVariable Long problemId,
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

        return userProblemService.addUserProblem(user.getUserId(), problemId, userProblems);
    }

    //delete a user problem
    @DeleteMapping("/{problemId}/delete")
    public ResponseEntity<?> deleteUserProblem(@PathVariable Long problemId) {

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

       userProblemService.deleteUserProblem(user.getUserId(), problemId);
       return ResponseEntity.ok().body("problem deleted from your list");
    }
}

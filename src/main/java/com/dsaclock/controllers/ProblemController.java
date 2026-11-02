package com.dsaclock.controllers;

import com.dsaclock.dto.UserProblemResponse;
import com.dsaclock.entities.Problems;
import com.dsaclock.entities.UserProblems;
import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.LeetcodeService;
import com.dsaclock.services.ProblemService;
import com.dsaclock.services.UserProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/problems")
@Tag(
        name = "Problems",
        description = "APIs to do operations on problems."
)
public class ProblemController {

    //problem service reference
    private final ProblemService problemService;

    //user problem service reference
    private final UserProblemService userProblemService;

    //user repo reference
    private final UserRepo userRepo;

    public ProblemController(ProblemService problemService,
                             UserProblemService userProblemService,
                             UserRepo userRepo) {
        this.problemService = problemService;
        this.userProblemService = userProblemService;
        this.userRepo = userRepo;
    }

    //get all problems
    @GetMapping
    @Operation(
            summary = "Get all problems",
            description = "Retrieves all problems available in database"
    )
    public List<Problems> getProblems() {
        return problemService.getProblem();
    }

    //get a problem by id
    @GetMapping("/{problemId}")
    @Operation(summary = "Get problem by ID")
    public Problems getProblems(@Parameter(description = "Unique identifier of problems")
                                    @PathVariable Long problemId) {

        return problemService.getProblem(problemId);
    }

    //get problem_details of a problem
    @GetMapping("/{problemId}/details")
    @Operation(summary = "Get problem details by ID")
    public String getProblem_details(@Parameter(description = "Unique identifier of problems")
                                         @PathVariable Long problemId) {
        return problemService.getProblem_details(problemId);
    }

    //add a new problem
    @PostMapping
    @Operation(summary = "Add new problem")
    public Problems addProblem(@RequestBody Problems problem) {
        problemService.addProblem(problem);
        return problem;
    }

    //update a problem
    @PutMapping("/{problemId}")
    @Operation(summary = "Update problem by ID")
    public ResponseEntity<?> updateProblem(@Parameter(description = "Unique identifier of problems")
            @PathVariable Long problemId,
            @RequestBody Problems problem) {

        problem.setProblemId(problemId);
        problemService.updateProblem(problem);

        return ResponseEntity.ok(problem);
    }

    //delete a problem
    @DeleteMapping("/{problemId}")
    @Operation(summary = "Delete problem by ID")
    public ResponseEntity<?> deleteProblem(@Parameter(description = "Unique identifier of problems")
                                               @PathVariable Long problemId) {

        problemService.deleteProblem(problemId);

        return ResponseEntity.ok().body("Problem deleted successfully!");
    }

    /*
    * user problem controll
    * ----------------------*/

    //add a problem to a user
    @PostMapping("{problemId}/add")
    @Operation(summary = "Add new problem")
    public ResponseEntity<Void> addUserProblem(@Parameter(description = "Unique identifier of problems")
                                                   @PathVariable Long problemId) { //for solved date

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

        userProblemService.addUserProblem(user.getUserId(), problemId);

        return ResponseEntity.ok().build();
    }

    //revise a user problem
    @PutMapping("{problemId}/revise")
    @Operation(summary = "Revise a problem by ID")
    public ResponseEntity<UserProblemResponse> reviseProblem(@Parameter(description = "Unique identifier of problems")
                                                                 @PathVariable Long problemId) {

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

        return ResponseEntity.ok(userProblemService
                .updateUserProblem(user.getUserId(), problemId));
    }

    //delete a user problem
    @DeleteMapping("/{problemId}/delete")
    @Operation(summary = "Delete a problem by ID")
    public ResponseEntity<?> deleteUserProblem(@Parameter(description = "Unique identifier of problems")
                                                   @PathVariable Long problemId) {

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

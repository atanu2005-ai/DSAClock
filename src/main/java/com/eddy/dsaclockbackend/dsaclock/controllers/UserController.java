package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.services.UserProblemService;
import com.eddy.dsaclockbackend.dsaclock.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService; //user service constructor
    private final UserProblemService userProblemService;
    public UserController(UserService userService, UserProblemService userProblemService) {
        this.userService = userService;
        this.userProblemService = userProblemService;
    }

    //get all user data
    @GetMapping
    public List<Users> getUser() {
        return userService.getUser();
    }

    @GetMapping("/{userId}")  //find single user with id
    public ResponseEntity<Users> getUser(@PathVariable Long userId) {

        Optional<Users> user = userService.getUser(userId);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //add new user
    @PostMapping
    public Users setUser(@RequestBody Users user) {
        return userService.setUser(user);
    }

    //update user if exists
    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(
            @PathVariable Long userId,
            @RequestBody Users user) {

        Optional<Users> thisUser = userService.getUser(userId);

        if (thisUser.isPresent()) {
            Users existingUser = thisUser.get();

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());

            return ResponseEntity.ok(userService.setUser(existingUser));
        }

        return ResponseEntity.notFound().build();
    }
    //delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Users> deleteUser(@PathVariable Long userId) {
        Optional<Users> thisUser = userService.getUser(userId);

        if(thisUser.isPresent()) {
            userService.deleteUser(userId);
            return ResponseEntity.ok(thisUser.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    /*user problem endpoints
     ----------------------*/

    //get all problems of the current user
    @GetMapping("/{userId}/problems")
    public List<Problems> getUserProblems(@PathVariable Long userId) {
        return userProblemService.getUserProblems(userId);
    }

    //add a problem to a user
    @PostMapping("/{userId}/problems/{problemId}")
    public ResponseEntity<UserProblems> addUserProblem(@PathVariable Long userId,
                                                       @PathVariable Long problemId,
                                                       @RequestBody UserProblems userProblems) {
        //this will return empty optional if the unique constraint already exists
        Optional<UserProblems> thisUserProblem = userProblemService.addUserProblem(userId, problemId, userProblems);

        if(thisUserProblem.isPresent()) {
            return ResponseEntity.ok(thisUserProblem.get());
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //
        }


    }

    //delete a user problem
    @DeleteMapping("/{userId}/problems/{userProblemId}")
    public ResponseEntity<Void> deleteUserProblem(@PathVariable Long userId,
                                                  @PathVariable Long userProblemId) {
        userProblemService.deleteUserProblem(userProblemId);
        return ResponseEntity.noContent().build();
    }

}

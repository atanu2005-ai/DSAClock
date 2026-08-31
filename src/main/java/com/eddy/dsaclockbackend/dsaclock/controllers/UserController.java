package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import com.eddy.dsaclockbackend.dsaclock.services.UserProblemService;
import com.eddy.dsaclockbackend.dsaclock.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService; //user service reference

    private final UserRepo userRepo; //user repo reference

    private final UserProblemService userProblemService; //user problem service reference

    public UserController(UserService userService, UserRepo userRepo,
                          UserProblemService userProblemService) {
        this.userService = userService;
        this.userRepo = userRepo;
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
    public ResponseEntity<?> setUser(@RequestBody Users user) {
        //checking if the email already exists in my table or not
        if(userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("An account with this email already exists");
        }else { //else add user
            userService.setUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Account created successfully");
        }
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
    @GetMapping("/me/problems")
    public List<Problems> getUserProblems() {

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

        return userProblemService.getUserProblems(user.getUserId()); //fetch user problems with id of the user
    }

}

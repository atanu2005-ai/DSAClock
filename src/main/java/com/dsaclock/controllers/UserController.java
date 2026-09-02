package com.dsaclock.controllers;

import com.dsaclock.dto.RegisterRequest;
import com.dsaclock.dto.UserResponse;
import com.dsaclock.entities.Problems;
import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.UserProblemService;
import com.dsaclock.services.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/me")  //find single user with id
    public Users getUserByUserId() {

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

        return userService.getUser(user.getUserId());
    }

    //add new user
    @PostMapping
    public UserResponse setUser(@Valid @RequestBody RegisterRequest request) {

        return userService.setUser(request);
    }

    //update user if exists
    @PutMapping("/me")
    public Users updateUser(@RequestBody Users user) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication(); //fetching current user's authentication object

        assert auth != null;
        String email = auth.getName(); //fetching email of current user using auth object

        Users thisUser =
                userRepo
                        .findByEmail(email) //create user object with th email
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userService.updateUser(thisUser.getUserId(), user);
    }
    //delete user
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser() {

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

        userService.deleteUser(user.getUserId());

        return ResponseEntity.ok().body("User deleted successfully!");
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

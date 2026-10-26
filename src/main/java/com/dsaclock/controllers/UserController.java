package com.dsaclock.controllers;

import com.dsaclock.dto.*;
import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.RevisionActivityService;
import com.dsaclock.services.UserProblemService;
import com.dsaclock.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService; //user service reference

    private final UserRepo userRepo; //user repo reference

    private final UserProblemService userProblemService; //user problem service reference

    private final RevisionActivityService revisionActivityService; //revision activity service reference

    public UserController(UserService userService, UserRepo userRepo,
                          UserProblemService userProblemService, RevisionActivityService revisionActivityService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.userProblemService = userProblemService;
        this.revisionActivityService = revisionActivityService;
    }

    //get all user data
    @GetMapping
    public List<Users> getUser() {
        return userService.getUser();
    }

    @GetMapping("/me")  //user profile request
    public ResponseEntity<UserProfileResponse> getUserProfile() {

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

        return ResponseEntity.ok(userService.getUserProfile(user.getUserId()));
    }

    //initiate registration
    @PostMapping
    public ResponseEntity<?> initiateRegistration(@Valid @RequestBody RegisterRequest request) {

        userService.initiateRegister(request.getUsername(),
                request.getEmail(),
                request.getPassword());

        return ResponseEntity.accepted().body(
                "OTP sent to your email. Please verify to complete registration");
    }

    //update user if exists
    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateUser(@RequestBody UserUpdateRequest request) {

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

        return userService.updateUser(thisUser.getUserId(), request);
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
    public List<UserProblemResponse> getUserProblems() {

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

    @GetMapping("/me/activity")
    public List<RevisionActivityResponse> getUserActivity() {
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

        return revisionActivityService.getActivities(user.getUserId());
    }

}

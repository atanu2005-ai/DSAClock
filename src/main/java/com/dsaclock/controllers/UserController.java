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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "APIs for user registration, profile management, and user-specific DSA activity."
)
public class UserController {

    private final UserService userService;

    private final UserRepo userRepo;

    private final UserProblemService userProblemService;

    private final RevisionActivityService revisionActivityService;

    public UserController(UserService userService, UserRepo userRepo,
                          UserProblemService userProblemService, RevisionActivityService revisionActivityService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.userProblemService = userProblemService;
        this.revisionActivityService = revisionActivityService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves the list of all registered users."
    )
    public List<Users> getUser() {
        return userService.getUser();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Retrieves the profile information of the currently authenticated user using their authentication credentials."
    )
    public ResponseEntity<UserProfileResponse> getUserProfile() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert auth != null;
        String email = auth.getName();

        Users user =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(userService.getUserProfile(user.getUserId()));
    }

    @PostMapping
    @Operation(
            summary = "Initiate user registration",
            description = "Initiates the registration process by accepting user credentials and sending an OTP to the provided email address. Registration is completed after OTP verification."
    )
    public ResponseEntity<?> initiateRegistration(
            @Valid
            @RequestBody(
                    description = "User registration details including username, email, and password.",
                    required = true
            )
            @org.springframework.web.bind.annotation.RequestBody RegisterRequest request
    ) {

        userService.initiateRegister(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.accepted().body(
                "OTP sent to your email. Please verify to complete registration");
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Update current user profile",
            description = "Updates the profile information of the currently authenticated user using the provided non-null fields."
    )
    public ResponseEntity<UserProfileResponse> updateUser(
            @RequestBody(
                    description = "Profile fields to update, such as username, about information, and social profile URLs.",
                    required = true
            )
            @org.springframework.web.bind.annotation.RequestBody UserUpdateRequest request
    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert auth != null;
        String email = auth.getName();

        Users thisUser =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userService.updateUser(thisUser.getUserId(), request);
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Delete current user account",
            description = "Permanently deletes the account associated with the currently authenticated user."
    )
    public ResponseEntity<?> deleteUser() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert auth != null;
        String email = auth.getName();

        Users user =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userService.deleteUser(user.getUserId());

        return ResponseEntity.ok().body("User deleted successfully!");
    }

    // User problem endpoints

    @GetMapping("/me/problems")
    @Operation(
            summary = "Get current user's problems",
            description = "Retrieves all DSA problems associated with the currently authenticated user, including their problem-tracking information."
    )
    public List<UserProblemResponse> getUserProblems() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert auth != null;
        String email = auth.getName();

        Users user =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userProblemService.getUserProblems(user.getUserId());
    }

    @GetMapping("/me/activity")
    @Operation(
            summary = "Get current user's revision activity",
            description = "Retrieves the revision activity history of the currently authenticated user, organized by activity date and revision count."
    )
    public List<RevisionActivityResponse> getUserActivity() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert auth != null;
        String email = auth.getName();

        Users user =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return revisionActivityService.getActivities(user.getUserId());
    }

}
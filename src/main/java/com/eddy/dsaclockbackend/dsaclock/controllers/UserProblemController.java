package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserProblemRepo;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import com.eddy.dsaclockbackend.dsaclock.services.UserProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("api/userProblems")
public class UserProblemController {

    //user problem service reference
    UserProblemService userProblemService;

    //user problem repo reference
    UserProblemRepo userProblemRepo;
    //user repo reference
    UserRepo userRepo;
    public UserProblemController(UserProblemService userProblemService,
                                 UserProblemRepo userProblemRepo,
                                 UserRepo userRepo) {
        this.userProblemService = userProblemService;
        this.userProblemRepo = userProblemRepo;
        this.userRepo = userRepo;
    }

    //delete a user problem
    @DeleteMapping("/{userProblemId}/delete")
    public ResponseEntity<Void> deleteUserProblem(@PathVariable Long userProblemId) {

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

        //searching for the relation of this user id with user problem id in user prob repo
        Optional<UserProblems> thisUserProblem =
        userProblemRepo.findByUser_UserIdAndUserProblemId(user.getUserId(), userProblemId);

        if(thisUserProblem.isPresent()) {
            userProblemService.deleteUserProblem(userProblemId);

            return ResponseEntity.noContent().build(); //successfully deleted
        }

        return ResponseEntity.notFound().build();
    }
}

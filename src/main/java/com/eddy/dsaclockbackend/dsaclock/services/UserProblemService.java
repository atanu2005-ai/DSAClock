package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserProblemRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProblemService {

    //constructor for user problem repo, user service and problem service
    UserProblemRepo userProblemRepo;
    UserService userService;
    ProblemService problemService;
    public UserProblemService(UserProblemRepo userProblemRepo,
                              UserService userService,
                              ProblemService problemService) {
        this.userProblemRepo = userProblemRepo;
        this.userService = userService;
        this.problemService = problemService;
    }


    //get all problems for a particular user
    public List<Problems> getUserProblems(Long userId) {
        return userProblemRepo.findByUserUserId(userId)
                .stream()
                .map(UserProblems::getProblem)
                .toList();
    }
    //add problem to user problems entity
    public Optional<UserProblems> addUserProblem(Long userId,
                                                 Long problemId,
                                                 UserProblems userProblem) {
        if(userProblemRepo.existsByUserUserIdAndProblemProblemId(userId, problemId)) {
            return  Optional.empty(); //returns empty optional if user id and problem id duo already exists
        }

        //extract the user and problem with the ids
        Users user = userService.getUser(userId).orElseThrow();
        Problems problem = problemService.getProblem(problemId).orElseThrow();

        //set the user and problem reference to the user problem
        userProblem.setUser(user);
        userProblem.setProblem(problem);

        return Optional.of(userProblemRepo.save(userProblem));
    }

    //delete a user problem object
    public void deleteUserProblem(Long userProblemId) {
        userProblemRepo.deleteById(userProblemId);
    }
}

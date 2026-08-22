package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import com.eddy.dsaclockbackend.dsaclock.repos.UserProblemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProblemService {
    UserProblemRepo userProblemRepo;
    public UserProblemService(UserProblemRepo userProblemRepo) {
        this.userProblemRepo = userProblemRepo;
    }


    //get all problems for a particular user
    public List<UserProblems> getUserProblems(Long userId) {
        return userProblemRepo.findByUserUserId(userId);
    }
    //add problem to user problems entity
    public void addUserProblem(UserProblems userProblem) {
        userProblemRepo.save(userProblem);
    }

    //delete a user problem object
    public void deleteUserProblem(Long userProblemId) {
        userProblemRepo.deleteById(userProblemId);
    }
}

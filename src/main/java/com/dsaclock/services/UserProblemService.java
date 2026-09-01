package com.dsaclock.services;

import com.dsaclock.entities.Problems;
import com.dsaclock.entities.UserProblems;
import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.ProblemNotFoundException;
import com.dsaclock.exceptions.UserNotFoundException;
import com.dsaclock.exceptions.UserProblemAlreadyExistsException;
import com.dsaclock.exceptions.UserProblemNotFoundException;
import com.dsaclock.repos.UserProblemRepo;
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
    public UserProblems addUserProblem(Long userId,
                                                 Long problemId,
                                                 UserProblems userProblem) {
        if(userProblemRepo.existsByUserUserIdAndProblemProblemId(userId, problemId)) { //throws exception
            throw new UserProblemAlreadyExistsException(
                    "This problem is already added to your list!");
        }

        //extract the user and problem with the ids
        Users user = userService.getUser(userId);//throws user not found exception in user service layer

        Problems problem = problemService.getProblem(problemId); //throws problem not found exception in
                                                                 //problem service layer

        //set the user and problem reference to the user problem
        userProblem.setUser(user);
        userProblem.setProblem(problem);

        return userProblemRepo.save(userProblem);
    }

    //delete a user problem object
    public void deleteUserProblem(Long userId, Long problemId) {

        if(!userProblemRepo.existsByUserUserIdAndProblemProblemId(userId, problemId)) {
            throw new UserProblemNotFoundException("This problem doesn't belong to your list");
        }
        userProblemRepo.deleteById(problemId);
    }
}

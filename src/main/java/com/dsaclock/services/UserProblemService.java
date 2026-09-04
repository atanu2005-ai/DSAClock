package com.dsaclock.services;

import com.dsaclock.entities.Problems;
import com.dsaclock.entities.UserProblems;
import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.UserProblemAlreadyExistsException;
import com.dsaclock.exceptions.UserProblemNotFoundException;
import com.dsaclock.repos.UserProblemRepo;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public UserProblems addUserProblem(Long userId, Long problemId) {

        if(userProblemRepo.existsByUserUserIdAndProblemProblemId(userId, problemId)) { //throws exception
            throw new UserProblemAlreadyExistsException(
                    "This problem is already added to your list!");
        }

        //extract the user and problem with the ids
        Users user = userService.getUser(userId);//throws user not found exception in user service layer

        Problems problem = problemService.getProblem(problemId); //throws problem not found exception in
                                                                 //problem service layer

        //set the user and problem reference to the user problem
        UserProblems userProblem = new UserProblems();
        userProblem.setUser(user);
        userProblem.setProblem(problem);
        userProblem.setRevision_count(0);

        LocalDate today = LocalDate.now(); //current date

        userProblem.setSolved_date(today);

        userProblem.setNext_revision_date(today.plusDays(2)); // revision after 2 days for first time adding

        return userProblemRepo.save(userProblem);
    }

    //update user problem (after a revision)
    public UserProblems updateUserProblem(Long userId, Long problemId) {
        UserProblems userProblems = userProblemRepo
                .findByUser_UserIdAndProblem_ProblemId(userId, problemId).orElseThrow(() ->
               new UserProblemNotFoundException(
                       "No problem with such ID found associated with the user") );

        int updated_count = userProblems.getRevision_count() + 1; //updated revision count

        userProblems.setRevision_count(updated_count); //update revision count

        //REVISION LOGIC
        LocalDate today = LocalDate.now(); //current date

        if(updated_count == 1) {
            userProblems.setNext_revision_date(today.plusDays(3)); //after 1 revision, next revision after 3 days
        }else if(updated_count > 1) {
            userProblems.setNext_revision_date(null);
        }

        userProblemRepo.save(userProblems);

        return userProblems;
    }

    //delete a user problem object
    public void deleteUserProblem(Long userId, Long problemId) {

        Optional<UserProblems> thisUserProblem = userProblemRepo
                .findByUser_UserIdAndProblem_ProblemId(userId, problemId);

       if(thisUserProblem.isPresent()) {
           userProblemRepo.delete(thisUserProblem.get());
       }else {
           throw new UserProblemNotFoundException("No such problem belongs to you");
       }
    }
}

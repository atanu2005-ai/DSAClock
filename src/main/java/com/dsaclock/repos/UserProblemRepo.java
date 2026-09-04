package com.dsaclock.repos;

import com.dsaclock.entities.UserProblems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProblemRepo extends JpaRepository<UserProblems, Long> {

    //to find all user problems with relation of the current user
    List<UserProblems> findByUserUserId(Long userId);

    Optional<UserProblems> findByUser_UserIdAndProblem_ProblemId(Long userId, Long problemId);

    //using unique constraint to check if this pair of user and problem exists
    boolean existsByUserUserIdAndProblemProblemId(Long userId, Long problemId);
}

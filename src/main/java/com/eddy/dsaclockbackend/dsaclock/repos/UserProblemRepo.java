package com.eddy.dsaclockbackend.dsaclock.repos;

import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProblemRepo extends JpaRepository<UserProblems, Long> {

    //to find all user problems with relation of the current user
    List<UserProblems> findByUserUserId(Long userId);
}

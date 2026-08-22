package com.eddy.dsaclockbackend.dsaclock.repos;

import com.eddy.dsaclockbackend.dsaclock.entities.UserProblems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemRepo extends JpaRepository<UserProblems, Long> {
}

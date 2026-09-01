package com.dsaclock.repos;

import com.dsaclock.entities.Problems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepo extends JpaRepository<Problems, Long> {

    Optional<Problems> findByProblemId(Long problemId); //return problem with id
}

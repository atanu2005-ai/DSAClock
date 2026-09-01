package com.dsaclock.repos;

import com.dsaclock.entities.Problems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepo extends JpaRepository<Problems, Long> {
}

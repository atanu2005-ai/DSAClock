package com.eddy.dsaclockbackend.dsaclock.repos;

import com.eddy.dsaclockbackend.dsaclock.entities.Problems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepo extends JpaRepository<Problems, Long> {
}

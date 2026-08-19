package com.eddy.dsaclockbackend.dsaclock.repos;

import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Long> {
}

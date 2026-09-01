package com.dsaclock.repos;

import com.dsaclock.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    //method to find user with existing email id
    boolean existsByEmail(String email);

    //method to get user object with email
    Optional<Users> findByEmail(String email);
}

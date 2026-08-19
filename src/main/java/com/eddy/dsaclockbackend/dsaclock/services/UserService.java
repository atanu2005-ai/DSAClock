package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.User;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Optional;
@Service
public class UserService {
    UserRepo userRepo;

    public User setUser(@RequestBody User user) {
        return userRepo.save(user);
    }
}

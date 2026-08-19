package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {
    UserRepo userRepo;

    public Users setUser(@RequestBody Users user) {
        return userRepo.save(user);
    }
}

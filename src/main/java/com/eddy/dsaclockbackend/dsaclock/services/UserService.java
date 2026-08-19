package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<Users> getUser() {  //find all student
        return userRepo.findAll();
    }

    public Optional<Users> getUser(Long id) { //find user by id
        return userRepo.findById(id);
    }

    public Users setUser(Users user) { //add new user
        return userRepo.save(user);
    }
}

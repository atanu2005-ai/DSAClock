package com.eddy.dsaclockbackend.dsaclock.services;

import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //return all users
    public List<Users> getUser() {  //find all student
        return userRepo.findAll();
    }

    //return a single user with id
    public Optional<Users> getUser(Long userId) { //find user by id
        return userRepo.findById(userId);
    }

    //check unique email in table
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    //add new user
    public Users setUser(Users user) { //add new user
        return userRepo.save(user);
    }

    //update a user data
    public Optional<Users> updateUser(Long userId) { return userRepo.findById(userId); }

    //delete user
    public void deleteUser(Long userId) {userRepo.deleteById(userId); }
}

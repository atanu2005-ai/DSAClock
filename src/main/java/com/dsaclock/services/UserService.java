package com.dsaclock.services;

import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    //user repo reference
    private final UserRepo userRepo;

    //password encoder reference
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword())); //encoding given password
        return userRepo.save(user);
    }

    //update a user data
    public Optional<Users> updateUser(Long userId) { return userRepo.findById(userId); }

    //delete user
    public void deleteUser(Long userId) {userRepo.deleteById(userId); }
}

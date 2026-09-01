package com.dsaclock.services;

import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.UserAlreadyExistsException;
import com.dsaclock.exceptions.UserNotFoundException;
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
    public Users getUser(Long userId) { //find user by id

       return userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found"));

    }

    //check unique email in table
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    //add new user
    public Users setUser(Users user) { //add new user
        user.setPassword(passwordEncoder.encode(user.getPassword())); //encoding given password

        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        return userRepo.save(user);
    }

    //update a user data
    public Users updateUser(Long userId, Users user) {

        Users existingUser = userRepo.findByEmail(user.getEmail()).orElseThrow(() ->
                new UserNotFoundException("This user doesn't even exist bro!"));

        user.setPassword(passwordEncoder.encode(user.getPassword())); //encoding given password

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        return existingUser;
    }

    //delete user
    public void deleteUser(Long userId) {userRepo.deleteById(userId); }
}

package com.dsaclock.services;

import com.dsaclock.dto.RegisterRequest;
import com.dsaclock.dto.UserResponse;
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
    public UserResponse setUser(RegisterRequest request) { //add new user

        Users user = new Users();

        //setting properties of request object to the new user object
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encoding given password

        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        //saving the new user object and keeping it as a new user object named savedUser
        Users savedUser = userRepo.save(user);

        //creating a user response object
        UserResponse response = new UserResponse();

        //setting saved user's properties such as id and username to the response object
        response.setUserId(savedUser.getUserId());
        response.setUsername(savedUser.getUsername());

        return response; //returning the response object
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

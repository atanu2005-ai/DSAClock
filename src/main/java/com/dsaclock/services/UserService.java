package com.dsaclock.services;

import com.dsaclock.dto.*;
import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.UserAlreadyExistsException;
import com.dsaclock.exceptions.UserNotFoundException;
import com.dsaclock.repos.UserProblemRepo;
import com.dsaclock.repos.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    //user repo reference
    private final UserRepo userRepo;

    //user problem repo reference
    private final UserProblemRepo userProblemRepo;

    //password encoder reference
    private final PasswordEncoder passwordEncoder;

    //authentication manager reference
    private final AuthenticationManager authenticationManager;

    //jwt service reference
    private final JwtService jwtService;

    public UserService(UserRepo userRepo, UserProblemRepo userProblemRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.userProblemRepo = userProblemRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    //return all users
    public List<Users> getUser() {  //find all student
        return userRepo.findAll();
    }

    //get a user obj
    public Users getUserById(Long userId) { //find user by id
        return userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found"));
    }

    //user profile
    public UserProfileResponse getUserProfile(Long userId) { //find user by id

        Users user = userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        int problemsSolved = userProblemRepo.countByUser_UserId(userId); //get total problems solved by user
        response.setTotalProblemsSolved(problemsSolved);
        response.setTotalRevisions(user.getUserRevisionCount()); //get total revisions by user

        return response;
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

    //USER LOGIN
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( //authenticate this user
                request.getEmail(), request.getPassword()));

        String token = jwtService.generateToken(request.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;
    }

    //update a user data
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {

        Users existingUser = userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("This user doesn't even exist bro!"));

        if(request.getUsername() != null) {
            existingUser.setUsername(request.getUsername()); //updating username if request obj have that
        }

        if(request.getEmail() != null) {
            existingUser.setEmail(request.getEmail()); //updating user email if request obj have that
        }

        if(request.getPassword() != null) {

            //updating password if the request obj have that
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Users user = userRepo.save(existingUser);

        UserResponse response = new UserResponse();

        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());

        return response;
    }

    //delete user
    public void deleteUser(Long userId) {userRepo.deleteById(userId); }
}

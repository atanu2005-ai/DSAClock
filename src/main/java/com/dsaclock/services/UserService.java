package com.dsaclock.services;

import com.dsaclock.dto.*;
import com.dsaclock.entities.RevisionActivity;
import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.UserAlreadyExistsException;
import com.dsaclock.exceptions.UserNotFoundException;
import com.dsaclock.repos.RevisionActivityRepo;
import com.dsaclock.repos.UserProblemRepo;
import com.dsaclock.repos.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    //Revision activity repo reference
    private final RevisionActivityRepo revisionActivityRepo;

    //email service reference
    private final EmailService emailService;

    private final Map<String, PendingRegistration> pendingRegistrationMap =
            new ConcurrentHashMap<>();

    public UserService(UserRepo userRepo, UserProblemRepo userProblemRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RevisionActivityRepo revisionActivityRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.userProblemRepo = userProblemRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.revisionActivityRepo = revisionActivityRepo;
        this.emailService = emailService;
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
        response.setAbout(user.getAbout());
        response.setLeetcodeProfileUrl(user.getLeetcodeProfileUrl());
        response.setGithubProfileUrl(user.getGithubProfileUrl());
        response.setLinkedinProfileUrl(user.getLinkedinProfileUrl());
        response.setTwitterProfileUrl(user.getTwitterProfileUrl());

        int problemsSolved = userProblemRepo.countByUser_UserId(userId); //get total problems solved by user
        response.setTotalProblemsSolved(problemsSolved);
        response.setTotalRevisions(user.getUserRevisionCount()); //get total revisions by user

        int revised = userProblemRepo.countByUserAndRevisionCountGreaterThan(user, 0);
        response.setRevised(revised); //(total solved / problems revised at least once)

        double percentage = problemsSolved == 0 ? 0 : (double) (revised * 100) / problemsSolved;
        response.setRevisedPercentage(percentage); //revised percentage

        int revisedAtLeastTwice = userProblemRepo.countByUserAndRevisionCountGreaterThan(user, 1);
        response.setRevisedAtLeastTwice(revisedAtLeastTwice); //problems revised at least twice

        double revisedTwicePercentage = problemsSolved ==
                0 ? 0 : (double) (revisedAtLeastTwice * 100) / problemsSolved;
        response.setRevisedTwicePercentage(revisedTwicePercentage); //revised twice percentage

        //CURRENT STREAK COUNTING ALGO
        List<RevisionActivity> list = revisionActivityRepo.findByUserUserId(userId);

        int currentStreak = 0;

        Set<LocalDate> activityDates = list.stream() //collecting all dates from the user's activity objs
                .map(RevisionActivity::getActivityRevisionDate)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();

        int daysAgo;
        LocalDate currentDate;
        if(activityDates.contains(today)) { //WHEN TODAY HAS ACTIVITY
            daysAgo = 0;
            currentDate = today;
        }else { //IF NO ACTIVITY TODAY, CHECK TILL LAST DATE
            daysAgo = 1;
            currentDate = today.minusDays(1);
        }
        while(activityDates.contains(currentDate)) {
            currentStreak++;
            daysAgo++;
            currentDate = today.minusDays(daysAgo);
        }

        response.setCurrentStreak(currentStreak); //setting calculated current streak

        //MAX STREAK ALGO
        int maxStreak = 0;
        int current = 0;
        int daysAgoForMaxStreak = 365 * 5;
        while(daysAgoForMaxStreak >= 0) {
            LocalDate date = LocalDate.now().minusDays(daysAgoForMaxStreak);

            if(activityDates.contains(date)) {
                current++;
            }else {
                current = 0;
            }

            maxStreak = Math.max(maxStreak, current);

            daysAgoForMaxStreak--;
        }

        response.setMaxStreak(maxStreak);

        return response;
    }

    //check unique email in table
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    //initiate user registration
    public void initiateRegister(String username, String email, String password) {

        //saving credentials in concurrent map until otp verification
        String encodedPassword = passwordEncoder.encode(password);

        PendingRegistration pending = new PendingRegistration(username, email, encodedPassword);

        pendingRegistrationMap.put(email.toLowerCase(), pending);

        emailService.sendOtp(email);
    }
    //add new user
    public UserResponse setUser(PendingRegistration pending) { //add new user

        Users user = new Users();

        //setting properties of request object to the new user object
        user.setUsername(pending.username());
        user.setEmail(pending.email());
        user.setPassword(pending.EncodedPassword()); // encoding given password

        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        //saving the new user object and keeping it as a new user object named savedUser
        Users savedUser = userRepo.save(user);

        System.out.println("user saved");

        //creating a user response object
        UserResponse response = new UserResponse();

        //setting saved user's properties such as id and username to the response object
        response.setUserId(savedUser.getUserId());
        response.setUsername(savedUser.getUsername());

        return response;
    }

    public PendingRegistration getPendingRegistration(String email) {
        return pendingRegistrationMap.get(email);
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

    //update a user details
    public ResponseEntity<UserProfileResponse> updateUser(Long userId, UserUpdateRequest request) {

        Users existingUser = userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("This user doesn't even exist bro!"));

        if(request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }

        if(request.getAbout() != null)  {
            existingUser.setAbout(request.getAbout());
        }

        if(request.getLeetcodeProfileUrl() != null) {
            existingUser.setLeetcodeProfileUrl(request.getLeetcodeProfileUrl());
        }

        if(request.getGithubProfileUrl() != null) {
            existingUser.setGithubProfileUrl(request.getGithubProfileUrl());
        }

        if(request.getLinkedinProfileUrl() != null) {
            existingUser.setLinkedinProfileUrl(request.getLinkedinProfileUrl());
        }

        if(request.getTwitterProfileUrl() != null) {
            existingUser.setTwitterProfileUrl(request.getTwitterProfileUrl());
        }

        userRepo.save(existingUser);

        return ResponseEntity.ok(getUserProfile(userId));
    }

    //delete user
    public void deleteUser(Long userId) {userRepo.deleteById(userId); }
}

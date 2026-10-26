package com.dsaclock.controllers;

import com.dsaclock.dto.LoginRequest;
import com.dsaclock.dto.LoginResponse;
import com.dsaclock.dto.UserResponse;
import com.dsaclock.dto.VerifyRequestBody;
import com.dsaclock.entities.Users;
import com.dsaclock.services.OtpService;
import com.dsaclock.services.PendingRegistration;
import com.dsaclock.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("api/")
public class AuthController {

    //user service reference
    private final UserService userService;

    //otp service reference
    private final OtpService otpService;

    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            return ResponseEntity.ok(userService.login(request));
        }catch (AuthenticationException e) {
            return
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<UserResponse> verifyOtp(@RequestBody VerifyRequestBody requestBody) {
        System.out.println("controller verify otp reached");
        boolean verified = otpService.verifyOtp(requestBody.email(), requestBody.otp());

        if(verified) {

            PendingRegistration pending = userService.getPendingRegistration(requestBody.email());
            UserResponse response = userService.setUser(pending);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

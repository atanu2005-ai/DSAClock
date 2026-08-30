package com.eddy.dsaclockbackend.dsaclock.dto;

import com.eddy.dsaclockbackend.dsaclock.services.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginRequest {

    private String email;
    private String password;

    //authentication manager reference
    private final AuthenticationManager authenticationManager;

    //jwt service reference
    private final JwtService jwtService;

    public LoginRequest(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostMapping("/login") //login mapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        //passing the token to manager to create an authenticated object if user gets authenticated
        //otherwise throws exception
        Authentication authentication = authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(request.getEmail()); //generation token using request body email

        return ResponseEntity.ok(jwt);

    }
}

package com.dsaclock.controllers;

import com.dsaclock.dto.LoginRequest;
import com.dsaclock.dto.LoginResponse;
import com.dsaclock.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AuthController {

    //user service reference
    final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
       return  userService.login(request);
    }
}

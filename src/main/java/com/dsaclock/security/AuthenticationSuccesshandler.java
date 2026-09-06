package com.dsaclock.security;

import com.dsaclock.dto.LoginResponse;
import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.JwtService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationSuccesshandler implements AuthenticationSuccessHandler {

    //user repo reference
    private final UserRepo userRepo;

    //jwt service reference
    private final JwtService jwtService;

    private final ObjectMapper  objectMapper;

    public AuthenticationSuccesshandler(UserRepo userRepo, JwtService jwtService, ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws NullPointerException, IOException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal(); //gets authenticated google user
        String email = oAuth2User.getAttribute("email");

        Optional<Users> thisUser = userRepo.findByEmail(email);

        if(thisUser.isPresent()) { //IF USER IS EXISTING
            Users existingUser = thisUser.get();

            String token = jwtService.generateToken(email); //generate jwt token with user email

            LoginResponse loginResponse = new LoginResponse();

            loginResponse.setToken(token);

            //as response will not convert into json automatically (not a rest controller)
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            //serializes dto into response body of login response
            objectMapper.writeValue(response.getWriter(), loginResponse);

        }else { //FOR NEW USERS THROUGH GOOGLE LOGIN

            String username = oAuth2User.getAttribute("name");

            Users user = new Users();
            user.setUsername(username);
            user.setEmail(email);
            user.setAuthProvider(Users.AuthProvider.GOOGLE);

            userRepo.save(user);

            //JWT part
            //--------
            String token = jwtService.generateToken(email); //generate jwt token with user email

            LoginResponse loginResponse = new LoginResponse();

            loginResponse.setToken(token);

            //as response will not convert into json automatically (not a rest controller)
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            //serializes dto into response body of login response
            objectMapper.writeValue(response.getWriter(), loginResponse);
        }
    }

}

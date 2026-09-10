package com.dsaclock.security;

import com.dsaclock.dto.LoginResponse;
import com.dsaclock.entities.Users;
import com.dsaclock.exceptions.UserAlreadyExistsException;
import com.dsaclock.exceptions.UserNotFoundException;
import com.dsaclock.repos.UserRepo;
import com.dsaclock.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
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

    //spring auth repo reference where we have stored the action parameter in the authorization request attributes
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest>
            oauth2AuthorizationRequestRepository;

    private final ObjectMapper  objectMapper;

    public AuthenticationSuccesshandler(UserRepo userRepo,
                                        JwtService jwtService,
                                        AuthorizationRequestRepository<OAuth2AuthorizationRequest>
                                                oauth2AuthorizationRequestRepository,
                                        ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.oauth2AuthorizationRequestRepository =
                oauth2AuthorizationRequestRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws NullPointerException, IOException {


        String action = (String) request.getSession().getAttribute("auth_action");//fetching action from the session attribute where
                                                                                        // we have stored it in the authorization request attributes

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal(); //gets authenticated google user
        String email = oAuth2User.getAttribute("email");

        Optional<Users> thisUser = userRepo.findByEmail(email);


        /*login or registration using Google allowance logic
        * --------------------------------------------------*/

        if("login".equals(action) && thisUser.isEmpty()) { //if user is not existing and trying to login

            response.sendRedirect("http://localhost:5173/login?error=user_not_found");

        }else if("login".equals(action) && thisUser.isPresent()) { //IF USER IS EXISTING
            Users existingUser = thisUser.get();

            String token = jwtService.generateToken(email); //generate jwt token with user email

            LoginResponse loginResponse = new LoginResponse();

            loginResponse.setToken(token);


            response.sendRedirect("http://localhost:5173/problems?token=" + token); //redirecting to problems after successful login

            //serializes dto into response body of login response
            objectMapper.writeValue(response.getWriter(), loginResponse);

        }else if("register".equals(action) && thisUser.isPresent()) {

            response.sendRedirect("http://localhost:5173/register?error=user_exists");

        }else if("register".equals(action) && thisUser.isEmpty()) {

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

            response.sendRedirect("http://localhost:5173/problems?token=" + token); //redirecting to problems after successful registration

            request.getSession().removeAttribute("auth_action"); //removing the action attribute from the session after successful login or registration
        }
    }

}

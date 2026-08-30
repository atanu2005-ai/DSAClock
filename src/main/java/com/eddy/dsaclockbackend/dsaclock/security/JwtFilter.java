package com.eddy.dsaclockbackend.dsaclock.security;

import com.eddy.dsaclockbackend.dsaclock.services.JwtService;
import com.eddy.dsaclockbackend.dsaclock.services.MyUserDetailsService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    //jwt service reference
    public final JwtService jwtService;

    //my user details service reference
    public final MyUserDetailsService  myUserDetailsService;

    public JwtFilter(JwtService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //jwt logic
        String authHeader = request.getHeader("Authorization"); //grab the header with token

        if(authHeader != null && authHeader.startsWith("Bearer ")) { //as our header must look like: "Bearer ..."
            String token = authHeader.substring(7); //cuts Bearer

            String userName = jwtService.extractEmail(token); //extract email from token after verifying authentication with the token

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName); //email here

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken( //creating authenticated object
                            userDetails, //passing user details
                            null, //keeping password null as already verified by jwt
                            userDetails.getAuthorities()); //passing request authorities of the user

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication); //passing authenticated object into spring
                                                        //security context

        }

        filterChain.doFilter(request, response); //moves to the next filter
    }
}

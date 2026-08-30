package com.eddy.dsaclockbackend.dsaclock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean //filter chain bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) //disabling csrf protection
                .authorizeHttpRequests(auth ->{

            //PUBLIC ENDPOINTS
            auth.requestMatchers("/api/problems/**").permitAll();

            //AUTHENTICATION FREE REGISTRATION
            auth.requestMatchers(HttpMethod.POST,("/api/users"));

            //AUTHENTICATED ENDPOINTS
            auth.anyRequest().authenticated();
        }).build();
    }

    @Bean //password encoder bean. Returns password encoder object
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //authentication bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}

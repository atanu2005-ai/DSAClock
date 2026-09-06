package com.dsaclock.config;

import com.dsaclock.security.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //jwt filter reference
    private final JwtFilter jwtFilter;

    //auth success handler reference
    private final AuthenticationSuccessHandler oauthsuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, AuthenticationSuccessHandler oauthsuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.oauthsuccessHandler = oauthsuccessHandler;
    }


    @Bean //filter chain bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) //disabling csrf protection
                .authorizeHttpRequests(auth ->{

            //PUBLIC ENDPOINTS
            auth.requestMatchers(HttpMethod.GET,("/api/problems/**")).permitAll();

            //AUTHENTICATION FREE REGISTRATION
            auth.requestMatchers(HttpMethod.POST,("/api/users")).permitAll();

            //AUTHENTICATION FREE LOGIN
            auth.requestMatchers(HttpMethod.POST,("/api/login")).permitAll();

            //AUTHENTICATED ENDPOINTS
            auth.anyRequest().authenticated();
        })
                .oauth2Login(oauth ->
                        oauth.successHandler(oauthsuccessHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //goes through jwt filter first
                .build();
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

package com.dsaclock.services;

import com.dsaclock.entities.Users;
import com.dsaclock.repos.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    //user repo reference
    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users thisUser = userRepo.findByEmail(username) //creating new user object if exists
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); //otherwise throw exception

        //extracting user's username, password from user object
        return User
                .withUsername(thisUser.getEmail())
                .password(thisUser.getPassword())
                .build();
    }
}

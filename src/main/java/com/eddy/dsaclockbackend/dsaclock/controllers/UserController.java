package com.eddy.dsaclockbackend.dsaclock.controllers;

import com.eddy.dsaclockbackend.dsaclock.entities.Users;
import com.eddy.dsaclockbackend.dsaclock.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    //add user
    @GetMapping //get all user data
    public List<Users> getUser() {
        return userService.getUser();
    }

    @GetMapping("/{userId}")  //find single user with id
    public ResponseEntity<Users> getUser(@PathVariable Long userId) {

        Optional<Users> user = userService.getUser(userId);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping //add new user
    public Users setUser(@RequestBody Users user) {
        return userService.setUser(user);
    }

    //update user if exists
    @PutMapping("/{user_id}")
    public ResponseEntity<Users> updateUser(
            @PathVariable Long id,
            @RequestBody Users user) {

        Optional<Users> thisUser = userService.getUser(id);

        if (thisUser.isPresent()) {
            Users existingUser = thisUser.get();

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());

            return ResponseEntity.ok(userService.setUser(existingUser));
        }

        return ResponseEntity.notFound().build();
    }
    //delete user
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Users> deleteUser(@PathVariable Long user_id) {
        Optional<Users> thisUser = userService.getUser(user_id);

        if(thisUser.isPresent()) {
            userService.deleteUser(user_id);
            return ResponseEntity.ok(thisUser.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}

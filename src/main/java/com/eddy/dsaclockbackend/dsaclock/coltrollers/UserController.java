package com.eddy.dsaclockbackend.dsaclock.coltrollers;

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

    @GetMapping("/{id}")  //find single user with id
    public ResponseEntity<Users> getUser(@PathVariable Long id) {

        Optional<Users> user = userService.getUser(id);
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
}

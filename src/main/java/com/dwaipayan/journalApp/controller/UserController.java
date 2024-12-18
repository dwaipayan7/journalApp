package com.dwaipayan.journalApp.controller;

import com.dwaipayan.journalApp.entity.User;
import com.dwaipayan.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Fetch all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    // Create a new user
    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.saveEntry(user);
    }

    // Update an existing user
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody User user
    ) {
        User userInDb = userService.findByUserName(username);

        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveEntry(userInDb);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}

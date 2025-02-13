package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.dto.requests.LoginRequest;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;

    //    Register
    @PostMapping("/registerUser")
    @CrossOrigin("http://localhost:5173")
    public User addUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }
//    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
//        userService.registerUser(user);
//        String token = userService.generateToken(user.getUsername());
//        return ResponseEntity.ok(token);
//    }

    //    Login
    @PostMapping("/loginUser")
    @CrossOrigin("http://localhost:5173")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
//        return userService.loginUser(loginRequest);
        String token = userService.generateToken(loginRequest.getUserId());
//        String token = userService.loginUser(loginRequest);
        System.out.println("Token : "+token);
        return ResponseEntity.ok(token);
    }
}

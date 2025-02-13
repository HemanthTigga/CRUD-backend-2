package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exceptions.DataMismatchException;
import com.example.demo.repository.UserRepo;
import com.example.demo.dto.requests.LoginRequest;
import com.example.demo.security.JwtUtil;
import com.example.demo.utils.HashUtil;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@Slf4j
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserDetailsService userDetailsService;

    public User registerUser(@Valid User user) {
        if (userRepo.existsById(user.getEmail())) {
            throw new ValidationException("Email is already registered.");
        }
        user.setEmail(user.getEmail());
        user.setUsername(HashUtil.hashData(user.getUsername()));
        user.setPassword(HashUtil.hashData(user.getPassword()));

        return userRepo.save(user);
    }

    //    public Boolean loginUser(LoginRequest loginRequest){
    public String loginUser(@Valid LoginRequest loginRequest) {
        System.out.println(loginRequest);
        Optional<User> user1 = userRepo.findById(loginRequest.getUserId());
        if (!user1.isPresent()) {
            throw new DataMismatchException("User not found");
        }
        User user = user1.get();

        if ((!user.getPassword().equals(HashUtil.hashData(loginRequest.getPassword())))) {
            throw new DataMismatchException("Incorrect email or password");
        }

//        return "Login successful";
        return jwtUtil.generateToken(user.getEmail());

//        Authenticate the user
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserId(),loginRequest.getPassword()));
//
////        Load User details
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserId());
//
//        System.out.println("Invalid request");
////        Generate and return JWT token
//        return jwtUtil.generateToken(userDetails.getUsername());
    }
    public String generateToken(String username){
        return jwtUtil.generateToken(username);
    }
}

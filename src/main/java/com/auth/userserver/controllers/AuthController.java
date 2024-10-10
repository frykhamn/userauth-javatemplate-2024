package com.auth.userserver.controllers;
import com.auth.userserver.dto.*;
import com.auth.userserver.exceptions.UserAlreadyExistsException;
import com.auth.userserver.exceptions.UserRegistrationException;
import com.auth.userserver.security.CustomUserDetails;
import com.auth.userserver.security.JwtUtil;
import com.auth.userserver.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Validated @RequestBody UserRegisterRequest request) {
        UserDto savedUser = userService.registerUser(request);

        UserResponse response = new UserResponse();
        response.setStatus("success");
        response.setMessage("User registered successfully");
        response.setUser(savedUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Exception Handlers
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<UserResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        UserResponse response = new UserResponse();
        response.setStatus("failed");
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<UserResponse> handleUserRegistrationException(UserRegistrationException ex) {
        UserResponse response = new UserResponse();
        response.setStatus("failed");
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}



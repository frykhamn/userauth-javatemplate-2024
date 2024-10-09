package com.auth.userserver.controllers;


import com.auth.userserver.dto.LoginRequest;
import com.auth.userserver.dto.UserDto;
import com.auth.userserver.dto.UserRegisterRequest;
import com.auth.userserver.dto.UserResponse;
import com.auth.userserver.entities.User;
import com.auth.userserver.exceptions.UserAlreadyExistsException;
import com.auth.userserver.exceptions.UserRegistrationException;
import com.auth.userserver.security.CustomUserDetailsServiceImpl;
import com.auth.userserver.security.JwtUtil;
import com.auth.userserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new Exception("User is disabled", e);
        } catch (LockedException e) {
            throw new Exception("User account is locked", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
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

package com.auth.userserver.exceptions;

import com.auth.userserver.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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


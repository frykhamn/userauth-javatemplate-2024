package com.auth.userserver.controller;

import com.auth.userserver.controllers.AuthController;
import com.auth.userserver.dto.JwtResponse;
import com.auth.userserver.dto.LoginRequest;
import com.auth.userserver.dto.UserRegisterRequest;
import com.auth.userserver.dto.UserDto;
import com.auth.userserver.dto.UserResponse;
import com.auth.userserver.services.UserService;
import com.auth.userserver.security.CustomUserDetails;
import com.auth.userserver.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginSuccess() throws Exception {
        // Given: Mocka indata
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Mocka CustomUserDetails
        CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);

        // Mocka Authentication-objektet
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        // Mocka authenticationManager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mocka jwtUtil.generateToken()
        when(jwtUtil.generateToken(mockUserDetails)).thenReturn("mock-jwt-token");

        // When: Kör login-metoden
        ResponseEntity<?> response = authController.login(loginRequest);

        // Then: Kontrollera att JWT-token returnerades
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("mock-jwt-token", jwtResponse.getToken());
    }

    @Test
    void testLoginFailureBadCredentials() throws Exception {
        // Given: Mocka indata
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        // Mocka ett undantag för felaktiga inloggningsuppgifter
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        // When & Then: Kontrollera att rätt undantag kastas
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authController.login(loginRequest);
        });

        assertEquals("Incorrect username or password", exception.getMessage());
    }


    @Test
    void testRegistrationSuccess(){
        // Given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUsername("newuser");
        userRegisterRequest.setPassword("password123");
        userRegisterRequest.setEmail("new@example.com");
        userRegisterRequest.setFirstName("New");
        userRegisterRequest.setLastName("User");
        userRegisterRequest.setPhoneNumber("1234567890");

        UserDto userDto = new UserDto();
        userDto.setUsername("newuser");

        when(userService.registerUser(any(UserRegisterRequest.class))).thenReturn(userDto);

        // When
        ResponseEntity<UserResponse> response = authController.registerUser(userRegisterRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newuser", response.getBody().getUser().getUsername());
        verify(userService).registerUser(userRegisterRequest);
    }
}

package com.auth.userserver.controller;

import com.auth.userserver.controllers.AuthController;
import com.auth.userserver.dto.LoginRequest;
import com.auth.userserver.security.CustomUserDetailsServiceImpl;
import com.auth.userserver.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsServiceImpl userDetailsService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController; // Anta att detta är din controller

    @Test
    void testLoginSuccess() throws Exception {
        // Given: Mocka indata
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        UserDetails mockUserDetails = mock(UserDetails.class);

        // Mocka authenticationManager och userDetailsService
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // Inga undantag betyder att autentiseringen lyckades
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(mockUserDetails)).thenReturn("mock-jwt-token");

        // When: Kör login-metoden
        ResponseEntity<?> response = authController.login(loginRequest);

        // Then: Kontrollera att JWT-token returnerades
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mock-jwt-token", response.getBody());
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
        Exception exception = assertThrows(Exception.class, () -> {
            authController.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Incorrect username or password"));
    }
}

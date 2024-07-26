package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.dto.LoginRequest;
import com.oop.owebforum.security.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;

    private AppUser appUser;
    private LoginRequest loginRequest;
    @BeforeEach
    void setUp() {
        appUser = AppUser.builder().
                id(1L).username("username").
                password("password").build();

    }

    @Test
    void testAuthenticateUser_BadCredentials() {
    }
}
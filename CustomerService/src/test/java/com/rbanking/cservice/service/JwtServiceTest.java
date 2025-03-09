package com.rbanking.cservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.rbanking.cservice.entities.Login;
import com.rbanking.cservice.entities.Role;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails auth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Use a valid key of length 256 bits (32 characters when base64 encoded)
        jwtService.secret = "aSdFgHiJkLmNoPqRsTuVwXyZ123456789012345678901234567890123456";
    }

    @Test
    public void testGenerateToken() {
        Login login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("password");
        login.setRole(Role.OPERATOR);
        login.setCustId(1L);

        String token = jwtService.generateToken(login);

        assertNotNull(token);
    }

    @Test
    public void testExtractUserName() {
        Login login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("password");
        login.setRole(Role.OPERATOR);
        login.setCustId(1L);

        String token = jwtService.generateToken(login);

        String userName = jwtService.extractUserName(token);

        assertEquals("test@example.com", userName);
    }

    @Test
    public void testExtractExpiration() {
        Login login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("password");
        login.setRole(Role.OPERATOR);
        login.setCustId(1L);

        String token = jwtService.generateToken(login);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    public void testIsTokenExpired_NotExpired() {
        Login login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("password");
        login.setRole(Role.OPERATOR);
        login.setCustId(1L);

        String token = jwtService.generateToken(login);

        Boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    public void testValidateToken() {
        Login login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("password");
        login.setRole(Role.OPERATOR);
        login.setCustId(1L);

        UserDetails auth = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(login);

        Boolean isValid = jwtService.validateToken(token, auth);

        assertTrue(isValid);
    }
}

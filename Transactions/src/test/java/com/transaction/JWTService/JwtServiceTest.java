package com.transaction.JWTService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.transaction.model.Login;
import com.transaction.model.Role;
import com.transaction.service.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private Key key;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a secure key
        ReflectionTestUtils.setField(jwtService, "secret", Encoders.BASE64.encode(key.getEncoded()));
    }

    @Test
    void testGenerateToken() {
        Login login = new Login(1L, "test@example.com", "password", Role.ADMIN);
        String token = jwtService.generateToken(login);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // JWT tokens typically start with "eyJ"
    }

    @Test
    void testExtractUserName() {
        String token = createTestToken("test@example.com");
        String userName = jwtService.extractUserName(token);

        assertEquals("test@example.com", userName);
    }

    @Test
    void testExtractExpiration() {
        String token = createTestToken("test@example.com");
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testIsTokenExpired() {
        String token = createTestToken("test@example.com");
        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testValidateToken() {
        String token = createTestToken("test@example.com");
        when(userDetails.getUsername()).thenReturn("test@example.com");

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    private String createTestToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "ADMIN");
        claims.put("custId", 1L);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 min
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

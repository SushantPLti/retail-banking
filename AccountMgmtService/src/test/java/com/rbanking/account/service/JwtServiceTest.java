package com.rbanking.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.rbanking.account.entities.Login;
import com.rbanking.account.entities.Role;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	@InjectMocks
	private JwtService jwtService;

	@Mock
	private Key mockKey;

	@Mock
	private Claims mockClaims;

	@Value("${jwt.secret}")
	private String secret;

	@BeforeEach
	void setUp() {
		jwtService.secret = "dGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBmb3IgdGVzdGluZw==";
	}

	@Test
	void testGenerateToken() {
		Login login = new Login();
		login.setEmail("user@example.com");
		login.setRole(Role.CUSTOMER);
		login.setCustId(123456L);

		String token = jwtService.generateToken(login);

		assertNotNull(token);
		assertFalse(token.isEmpty());
	}

	@Test
	void testExtractUserName() {
		String token = jwtService.generateToken(getSampleLogin());

		String userName = jwtService.extractUserName(token);

		assertEquals("user@example.com", userName);
	}

	@Test
	void testExtractExpiration() {
		String token = jwtService.generateToken(getSampleLogin());

		Date expiration = jwtService.extractExpiration(token);

		assertNotNull(expiration);
		assertTrue(expiration.after(new Date()));
	}

	@Test
	void testIsTokenExpired() {
		String token = jwtService.generateToken(getSampleLogin());

		boolean isExpired = jwtService.isTokenExpired(token);

		assertFalse(isExpired);
	}

	@Test
	void testValidateToken() {
		String token = jwtService.generateToken(getSampleLogin());

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getUsername()).thenReturn("user@example.com");

		boolean isValid = jwtService.validateToken(token, userDetails);

		assertTrue(isValid);
	}

	private Login getSampleLogin() {
		Login login = new Login();
		login.setEmail("user@example.com");
		login.setRole(Role.CUSTOMER);
		login.setCustId(123456L);
		return login;
	}
}

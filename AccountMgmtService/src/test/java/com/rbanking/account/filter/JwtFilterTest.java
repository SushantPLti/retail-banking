package com.rbanking.account.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.rbanking.account.service.JwtService;
import com.rbanking.account.service.UserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

	@InjectMocks
	private JwtFilter jwtFilter;

	@Mock
	private JwtService jwtService;

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private UserDetailService userDetailService;

	@Mock
	private UserDetails userDetails;

	@BeforeEach
	void setUp() throws Exception {
		Field jwtServiceField = JwtFilter.class.getDeclaredField("jwtService");
		jwtServiceField.setAccessible(true);
		jwtServiceField.set(jwtFilter, jwtService);

		Field applicationContextField = JwtFilter.class.getDeclaredField("applicationContext");
		applicationContextField.setAccessible(true);
		applicationContextField.set(jwtFilter, applicationContext);

		when(applicationContext.getBean(UserDetailService.class)).thenReturn(userDetailService);
	}

	@Test
	void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
		String token = "valid-token";
		String userName = "test@example.com";

		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtService.extractUserName(token)).thenReturn(userName);
		when(userDetailService.loadUserByUsername(userName)).thenReturn(userDetails);
		when(jwtService.validateToken(token, userDetails)).thenReturn(true);
		when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(jwtService).extractUserName(token);
		verify(userDetailService).loadUserByUsername(userName);
		verify(jwtService).validateToken(token, userDetails);
		verify(filterChain).doFilter(request, response);
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
		String token = "invalid-token";
		String userName = "test@example.com";

		when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
		when(jwtService.extractUserName(token)).thenReturn(userName);
		when(userDetailService.loadUserByUsername(userName)).thenReturn(userDetails);
		when(jwtService.validateToken(token, userDetails)).thenReturn(false);

		jwtFilter.doFilterInternal(request, response, filterChain);

		verify(jwtService).extractUserName(token);
		verify(userDetailService).loadUserByUsername(userName);
		verify(jwtService).validateToken(token, userDetails);
		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

}

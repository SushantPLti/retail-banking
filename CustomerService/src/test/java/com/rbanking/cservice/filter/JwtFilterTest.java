package com.rbanking.cservice.filter;

import com.rbanking.cservice.service.JwtService;
import com.rbanking.cservice.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private UserDetailService userDetailService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationContext.getBean(UserDetailService.class)).thenReturn(userDetailService);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid-token";
        String userName = "john.doe@example.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUserName(token)).thenReturn(userName);
        when(jwtService.validateToken(any(String.class), any(UserDetails.class))).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailService.loadUserByEmail(userName)).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUserName(token);
        verify(jwtService).validateToken(token, userDetails);
        verify(userDetailService).loadUserByEmail(userName);
        verify(filterChain).doFilter(request, response);

        // Verify that authentication is set in the security context
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userDetails, authToken.getPrincipal());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalid-token";
        String userName = "john.doe@example.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUserName(token)).thenReturn(userName);
        when(jwtService.validateToken(any(String.class), any(UserDetails.class))).thenReturn(false);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailService.loadUserByEmail(userName)).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUserName(token);
        verify(jwtService).validateToken(token, userDetails);
        verify(userDetailService).loadUserByEmail(userName);
        verify(filterChain).doFilter(request, response);

        // Verify that no authentication is set in the security context
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

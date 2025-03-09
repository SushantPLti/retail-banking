package com.transaction.filter;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.lenient;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.transaction.service.JwtService;
import com.transaction.service.UserDetailService;

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
    void setUp() {
        lenient().when(applicationContext.getBean(UserDetailService.class)).thenReturn(userDetailService);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "validToken";
        String userName = "user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserName(token)).thenReturn(userName);
        when(userDetailService.loadUserByUsername(userName)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUserName(token);
        verify(userDetailService, times(1)).loadUserByUsername(userName);
        verify(jwtService, times(1)).validateToken(token, userDetails);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalidToken";
        String userName = "user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserName(token)).thenReturn(userName);
        when(userDetailService.loadUserByUsername(userName)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUserName(token);
        verify(userDetailService, times(1)).loadUserByUsername(userName);
        verify(jwtService, times(1)).validateToken(token, userDetails);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(0)).extractUserName(null);
        verify(userDetailService, times(0)).loadUserByUsername(null);
        verify(jwtService, times(0)).validateToken(null, null);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
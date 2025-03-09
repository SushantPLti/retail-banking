package com.transaction.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.transaction.filter.JwtFilter;
import com.transaction.service.UserDetailService;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private UserDetailService userDetailService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @BeforeEach
    void setUp() throws Exception {
        lenient().doReturn(userDetailService).when(applicationContext).getBean(UserDetailService.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        lenient().doReturn(authenticationManager).when(authenticationConfiguration).getAuthenticationManager();
    }


    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();
        assertNotNull(authenticationProvider, "AuthenticationProvider should not be null");
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);
        assertNotNull(authenticationManager, "AuthenticationManager should not be null");
    }

    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
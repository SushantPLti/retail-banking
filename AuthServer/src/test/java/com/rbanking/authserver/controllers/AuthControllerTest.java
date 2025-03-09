package com.rbanking.authserver.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.rbanking.authserver.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rbanking.authserver.model.AuthModel;
import com.rbanking.authserver.model.AuthResponse;
import com.rbanking.authserver.service.AuthService;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);
    }

    @Test
    void testLogin() throws Exception {
        AuthModel authModel = new AuthModel();
        authModel.setCustID(12345678L);
        authModel.setPassword("password");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setTokenType("dummyToken");

        when(authService.generateToken(any(AuthModel.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(authModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dummyToken", response.getBody().getTokenType());
    }
}

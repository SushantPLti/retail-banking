package com.rbanking.authserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.rbanking.authserver.dto.LoginDTO;
import com.rbanking.authserver.exception.CustomerInactiveException;
import com.rbanking.authserver.exception.UnauthorizedException;
import com.rbanking.authserver.feign.CustomerClient;
import com.rbanking.authserver.model.AuthModel;
import com.rbanking.authserver.model.AuthResponse;
import com.rbanking.authserver.service.AuthService;
import com.rbanking.authserver.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private JwtService jwtService;

    private AuthModel authModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authModel = new AuthModel();
        authModel.setCustID(12344556L);
        authModel.setPassword("password");
    }

    @Test
    public void testGenerateToken_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCustId(12345677L);
        loginDTO.setPassword("password");

        when(customerClient.validateCustomer(any(AuthModel.class))).thenReturn(loginDTO);
        when(jwtService.generateToken(loginDTO)).thenReturn("dummyToken");

        AuthResponse response = authService.generateToken(authModel);

        assertEquals("dummyToken", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    public void testGenerateToken_UnauthorizedException() throws Exception {
        when(customerClient.validateCustomer(any(AuthModel.class))).thenReturn(null);

        Exception thrownException = assertThrows(Exception.class, () -> {
            authService.generateToken(authModel);
        });

        assertEquals("No Customer Found with this custId 12344556", thrownException.getMessage());
    }

    @Test
    public void testGenerateToken_CustomerInactiveException() throws Exception {
        when(customerClient.validateCustomer(any(AuthModel.class))).thenThrow(new RuntimeException("account is inactive"));

        CustomerInactiveException exception = assertThrows(CustomerInactiveException.class, () -> {
            authService.generateToken(authModel);
        });

        assertEquals("account is inactive", exception.getMessage());
    }
}

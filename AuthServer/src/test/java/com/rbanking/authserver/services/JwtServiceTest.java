package com.rbanking.authserver.services;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.rbanking.authserver.dto.LoginDTO;
import com.rbanking.authserver.dto.Role;
import com.rbanking.authserver.service.JwtService;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import static org.mockito.Mockito.lenient;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    
    @Mock
    private LoginDTO loginDTO;

    private String secret = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg";

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        lenient().when(loginDTO.getEmail()).thenReturn("test@example.com");
        lenient().when(loginDTO.getRole()).thenReturn(Role.ADMIN); // Assuming Role is a String
        lenient().when(loginDTO.getCustId()).thenReturn((long) 12345);
    }

    @Test
    public void testGenerateToken() {
        String token = jwtService.generateToken(loginDTO);
        assertNotNull(token);
        // Additional assertions can be added to verify the token structure and claims
    }

    @Test
    public void testGetSignKey() {
        Key key = jwtService.getSignKey();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key expectedKey = Keys.hmacShaKeyFor(keyBytes);

        assertEquals(expectedKey, key);
    }
}
package com.rbanking.authserver.DTOs;

import org.junit.jupiter.api.Test;

import com.rbanking.authserver.dto.LoginDTO;
import com.rbanking.authserver.dto.Role;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest {

    @Test
    public void testNoArgsConstructor() {
        LoginDTO loginDTO = new LoginDTO();
        assertNotNull(loginDTO);
    }

    @Test
    public void testAllArgsConstructor() {
        LoginDTO loginDTO = new LoginDTO(1L, "test@example.com", "password123", Role.ADMIN);
        assertEquals(1L, loginDTO.getCustId());
        assertEquals("test@example.com", loginDTO.getEmail());
        assertEquals("password123", loginDTO.getPassword());
        assertEquals(Role.ADMIN, loginDTO.getRole());
    }

    @Test
    public void testSettersAndGetters() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setCustId(1L);
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");
        loginDTO.setRole(Role.ADMIN);

        assertEquals(1L, loginDTO.getCustId());
        assertEquals("test@example.com", loginDTO.getEmail());
        assertEquals("password123", loginDTO.getPassword());
        assertEquals(Role.ADMIN, loginDTO.getRole());
    }

    @Test
    public void testEmailSetter() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", loginDTO.getEmail());
    }

    @Test
    public void testPasswordSetter() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("newpassword123");
        assertEquals("newpassword123", loginDTO.getPassword());
    }

    @Test
    public void testRoleSetter() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, loginDTO.getRole());
    }
}
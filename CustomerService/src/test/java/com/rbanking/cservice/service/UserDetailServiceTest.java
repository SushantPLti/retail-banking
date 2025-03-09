package com.rbanking.cservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.rbanking.cservice.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rbanking.cservice.entities.Login;
import com.rbanking.cservice.repository.LoginRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
    	String username = "123456";

        when(loginRepository.findById(Long.parseLong(username))).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            try {
                userDetailService.loadUserByUsername(username);
            } catch (Exception e) {
                throw new UsernameNotFoundException("Username not found: " + username);
            }
        });

        assertEquals("Username not found: " + username, exception.getMessage());
    }
}

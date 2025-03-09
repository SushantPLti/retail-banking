package com.transaction.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordConfigTest {

    private PasswordConfig passwordConfig = new PasswordConfig();

    @Test
    void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = passwordConfig.encode();
        assertNotNull(encoder, "BCryptPasswordEncoder should not be null");

        // Test encoding a password
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword), "Encoded password should match the raw password");
    }
}
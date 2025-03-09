package com.rbanking.cservice.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void testValidPassword() {
        // Valid password
        String password = "Valid@123";
        assertTrue(passwordValidator.isValid(password, context));
    }

    @Test
    public void testInvalidPassword() {
        // Invalid password - no uppercase letter
        String passwordNoUpperCase = "invalid@123";
        assertFalse(passwordValidator.isValid(passwordNoUpperCase, context));

        // Invalid password - no lowercase letter
        String passwordNoLowerCase = "INVALID@123";
        assertFalse(passwordValidator.isValid(passwordNoLowerCase, context));

        // Invalid password - no number
        String passwordNoNumber = "Invalid@abc";
        assertFalse(passwordValidator.isValid(passwordNoNumber, context));

        // Invalid password - no special character
        String passwordNoSpecialChar = "Invalid123";
        assertFalse(passwordValidator.isValid(passwordNoSpecialChar, context));

        // Invalid password - less than 8 characters
        String shortPassword = "V@1a";
        assertFalse(passwordValidator.isValid(shortPassword, context));
    }

    @Test
    public void testNullOrEmptyPassword() {
        // Null password
        String nullPassword = null;
        assertFalse(passwordValidator.isValid(nullPassword, context));

        // Empty password
        String emptyPassword = "";
        assertFalse(passwordValidator.isValid(emptyPassword, context));
    }
}

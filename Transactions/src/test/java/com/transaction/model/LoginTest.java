package com.transaction.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class LoginTest {

    @Test
    void testConstructorAndGetters() {
        Long custId = 1L;
        String email = "test@example.com";
        String password = "password";
        Role role = Role.ADMIN;

        Login login = new Login(custId, email, password, role);

        assertEquals(custId, login.getCustId());
        assertEquals(email, login.getEmail());
        assertEquals(password, login.getPassword());
        assertEquals(role, login.getRole());
    }

    @Test
    void testSetters() {
        Login login = new Login();

        Long custId = 1L;
        String email = "test@example.com";
        String password = "password";
        Role role = Role.ADMIN;

        login.setCustId(custId);
        login.setEmail(email);
        login.setPassword(password);
        login.setRole(role);

        assertEquals(custId, login.getCustId());
        assertEquals(email, login.getEmail());
        assertEquals(password, login.getPassword());
        assertEquals(role, login.getRole());
    }

    @Test
    void testDefaultConstructor() {
        Login login = new Login();

        assertNull(login.getCustId());
        assertNull(login.getEmail());
        assertNull(login.getPassword());
        assertNull(login.getRole());
        assertFalse(login.isLoggedOut());
    }
}

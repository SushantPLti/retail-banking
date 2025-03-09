package com.transaction.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.transaction.model.ErrorResponse;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleAccountNotFound() {
        AccountNotFoundException ex = new AccountNotFoundException("Account not found");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccountNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getResponseCode());
        assertEquals("Account not found", response.getBody().getMessage());
        assertEquals(LocalDateTime.now().getYear(), response.getBody().getDateTime().getYear());
    }

    @Test
    void testHandleInsufficientBalance() {
        InsufficientBalanceException ex = new InsufficientBalanceException("Insufficient balance");
        ResponseEntity<String> response = globalExceptionHandler.handleInsuficientBalance(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient balance", response.getBody());
    }
}
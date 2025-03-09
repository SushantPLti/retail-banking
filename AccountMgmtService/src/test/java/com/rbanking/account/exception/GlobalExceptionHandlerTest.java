package com.rbanking.account.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.rbanking.account.model.ErrorResponse;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("field", "Field is invalid")));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getResponseCode());
        assertEquals("Field is invalid", response.getBody().getMessage());
    }

    @Test
    public void testHandleAccountNotFound() {
        AccountNotFoundException ex = new AccountNotFoundException("Account not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccountNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandleDateIntegrity() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDateIntegrity(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Data integrity violation", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandleAllExceptions() {
        Exception ex = new Exception("Internal server error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testMinimumBalanceException() {
        MinBalanceException ex = new MinBalanceException("Minimum balance not met");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.minimumBalaceException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Minimum balance not met", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getResponseCode());
    }
}

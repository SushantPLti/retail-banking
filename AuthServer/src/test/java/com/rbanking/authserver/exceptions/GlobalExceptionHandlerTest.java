package com.rbanking.authserver.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rbanking.authserver.exception.CustomerInactiveException;
import com.rbanking.authserver.exception.GlobalExceptionHandler;
import com.rbanking.authserver.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.rbanking.authserver.model.ErrorResponse;

import java.util.Collections;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException mockMethodArgumentNotValidException;

    @Mock
    private UnauthorizedException mockUnauthorizedException;

    @Mock
    private CustomerInactiveException mockCustomerInactiveException;

    @Mock
    private BindingResult mockBindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        ObjectError error = new ObjectError("field", "Field is invalid");
        when(mockBindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));
        when(mockMethodArgumentNotValidException.getBindingResult()).thenReturn(mockBindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(mockMethodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getResponseCode());
        assertEquals("Field is invalid", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleAccountNotFound() {
        when(mockUnauthorizedException.getMessage()).thenReturn("Unauthorized access");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccountNotFound(mockUnauthorizedException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().getResponseCode());
        assertEquals("Unauthorized access", response.getBody().getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testHandleCustomerInactiveException() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomerInactiveException(mockCustomerInactiveException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().getResponseCode());
        assertEquals("Your account is currently inactive. Please contact customer support for assistance.", response.getBody().getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testHandleAllExceptions() {
        Exception mockException = new Exception("Internal server error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(mockException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getResponseCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}


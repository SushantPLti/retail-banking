package com.rbanking.cservice.exception;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.rbanking.cservice.customer.model.ErrorResponse;

import java.util.Collections;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleEmailAlreadyExists() {
        EmailException ex = new EmailException("Email already exists");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEmailAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandleContactNumberAlreadyExists() {
        ContactNumberException ex = new ContactNumberException("Contact number already exists");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleContactNumberAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Contact number already exists", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandleCustomerNotFound() {
        CustomerNotFoundException ex = new CustomerNotFoundException("Customer not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomerNotFound(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Customer not found", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandlePanNumberAlreadyExists() {
        PanNumberException ex = new PanNumberException("PAN number already exists");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlePanNumberAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("PAN number already exists", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getResponseCode());
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        Mockito.when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("field", "Field is invalid")));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getResponseCode());
        assertEquals("Field is invalid", response.getBody().getMessage());
    }

    @Test
    public void testHandleAllExceptions() {
        Exception ex = new Exception("Internal server error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getResponseCode());
    }
}

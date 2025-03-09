package com.transaction.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.transaction.model.ErrorResponse;

/**
 * GlobalExceptionHandler is a controller advice class that handles exceptions
 * thrown by the application and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AccountNotFoundException and returns a response entity with an
     * error response containing the exception message.
     *
     * @param ex the AccountNotFoundException thrown when an account is not found
     * @return a ResponseEntity with an ErrorResponse and a NOT_FOUND status
     */
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<String> handleInsuficientBalance(InsufficientBalanceException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}

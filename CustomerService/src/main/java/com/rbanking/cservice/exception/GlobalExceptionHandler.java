package com.rbanking.cservice.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rbanking.cservice.customer.model.ErrorResponse;

/**
 * Global exception handler for handling various types of exceptions.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles EmailException.
	 * 
	 * @param ex the EmailException
	 */
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.CONFLICT.value());

		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Handles ContactNumberException.
	 * 
	 * @param ex the ContactNumberException
	 */

	@ExceptionHandler(ContactNumberException.class)
	public ResponseEntity<ErrorResponse> handleContactNumberAlreadyExists(ContactNumberException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.CONFLICT.value());

		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Handles CustomerNotFoundException.
	 * 
	 * @param ex the CustomerNotFoundException
	 */

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());

		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PanNumberException.class)
	public ResponseEntity<ErrorResponse> handlePanNumberAlreadyExists(PanNumberException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.CONFLICT.value());

		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Handles MethodArgumentNotValidException and returns a response entity with an
	 * error response containing the validation error messages.
	 *
	 * @param ex the MethodArgumentNotValidException thrown when method argument
	 *           validation fails
	 * @return a ResponseEntity with an ErrorResponse and a BAD_REQUEST status
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());

		String errorMsg = ex.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage())
//				.map(x -> environment.getProperty(x.getDefaultMessage()))
				.collect(Collectors.joining(", "));

		errorResponse.setMessage(errorMsg);
		errorResponse.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

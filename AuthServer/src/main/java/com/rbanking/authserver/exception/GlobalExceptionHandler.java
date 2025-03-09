package com.rbanking.authserver.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rbanking.authserver.model.ErrorResponse;

/**
 * GlobalExceptionHandler class to handle global exceptions in the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles MethodArgumentNotValidException and returns a custom error response.
	 *
	 * @param ex the MethodArgumentNotValidException
	 * @return a ResponseEntity containing the custom error response
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

	/**
	 * Handles UnauthorizedException and returns a custom error response.
	 *
	 * @param ex the UnauthorizedException
	 * @return a ResponseEntity containing the custom error response
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFound(UnauthorizedException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles CustomerInactiveException and returns a custom error response.
	 *
	 * @param ex the CustomerInactiveException
	 * @return a ResponseEntity containing the custom error response
	 */
	@ExceptionHandler(CustomerInactiveException.class)
	public ResponseEntity<ErrorResponse> handleCustomerInactiveException(CustomerInactiveException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setMessage("Your account is currently inactive. Please contact customer support for assistance.");
		errorResponse.setDateTime(LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
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

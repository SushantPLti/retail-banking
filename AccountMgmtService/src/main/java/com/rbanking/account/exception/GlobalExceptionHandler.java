package com.rbanking.account.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rbanking.account.model.ErrorResponse;

/**
 * GlobalExceptionHandler is a controller advice class that handles exceptions
 * thrown by the application and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
		logger.error("{}",errorMsg);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles AccountNotFoundException and returns a response entity with an error
	 * response containing the exception message.
	 *
	 * @param ex the AccountNotFoundException thrown when an account is not found
	 * @return a ResponseEntity with an ErrorResponse and a NOT_FOUND status
	 */
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		logger.error("{}",ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles DataIntegrityViolationException and returns a response entity with an
	 * error response containing the exception message.
	 *
	 * @param ex the DataIntegrityViolationException thrown when there is a data
	 *           integrity violation
	 * @return a ResponseEntity with an ErrorResponse and a CONFLICT status
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDateIntegrity(DataIntegrityViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.CONFLICT.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		logger.error("{}",ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		logger.error("{}",ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MinBalanceException.class)
	public ResponseEntity<ErrorResponse> minimumBalaceException(MinBalanceException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setDateTime(LocalDateTime.now());
		logger.error("{}",ex.getMessage());
		return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
	}
}

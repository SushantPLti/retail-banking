package com.rbanking.cservice.exception;

/**
 * Custom exception for handling email already exists.
 */
public class EmailException extends RuntimeException{
	public EmailException(String message) {
		super(message);
	}
}

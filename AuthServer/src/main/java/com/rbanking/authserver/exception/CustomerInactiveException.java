package com.rbanking.authserver.exception;

/**
 * Custom exception for handling cases where a customer is not found.
 */
public class CustomerInactiveException extends RuntimeException{
	public CustomerInactiveException(String message) {
		super(message);
	}

}

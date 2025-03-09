package com.rbanking.cservice.exception;

/**
 * Custom exception for handling cases where a customer is not found.
 */
public class CustomerNotFoundException extends RuntimeException{
	public CustomerNotFoundException(String message) {
		super(message);
	}

}

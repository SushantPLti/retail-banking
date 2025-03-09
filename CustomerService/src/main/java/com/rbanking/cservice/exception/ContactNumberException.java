package com.rbanking.cservice.exception;


/**
 * Custom exception for handling contact number already exists.
 */
public class ContactNumberException extends RuntimeException{
	public ContactNumberException(String message) {
		super(message);
	}
}

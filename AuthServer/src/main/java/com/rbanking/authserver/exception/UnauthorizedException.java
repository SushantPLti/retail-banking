package com.rbanking.authserver.exception;

public class UnauthorizedException extends RuntimeException {

	/**
	 * Exception thrown when an account is not found.
	 * <p>
	 * This exception is a runtime exception and indicates that an attempt to find
	 * an account has failed because the account does not exist. This can be used to
	 * signal errors in account retrieval operations.
	 * </p>
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(message);
	}

}

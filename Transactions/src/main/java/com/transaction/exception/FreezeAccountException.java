package com.transaction.exception;

public class FreezeAccountException extends Exception {
	private static final long serialVersionUID = 1L;

    public FreezeAccountException(String message) {
        super(message);
    }
}

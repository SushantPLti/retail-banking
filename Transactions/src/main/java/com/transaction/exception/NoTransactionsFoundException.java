package com.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoTransactionsFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoTransactionsFoundException() {
        super("Transaction not found.");
    }

    public NoTransactionsFoundException(String message) {
        super(message);
    }
}
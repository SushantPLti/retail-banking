package com.rbanking.account.exception;

public class MinBalanceException  extends RuntimeException{

	private static final long serialVersionUID = 6467220491921690236L;

	public MinBalanceException(String message) {
		super(message);
	}

}

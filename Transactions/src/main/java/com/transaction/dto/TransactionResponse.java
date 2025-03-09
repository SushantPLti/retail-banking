package com.transaction.dto;

import org.springframework.http.HttpStatus;

public class TransactionResponse<T> {
	public TransactionResponse(T data, String message, HttpStatus status) {
		super();
		this.data = data;
		this.message = message;
		this.status = status;
	}
	private T data;
	private String message;
	private HttpStatus status;
	public T getData() {
		return data;
	}
	public String getMessage() {
		return message;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setData(T data) {
		this.data = data;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}

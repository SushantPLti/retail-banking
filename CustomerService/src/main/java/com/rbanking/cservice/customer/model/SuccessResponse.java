package com.rbanking.cservice.customer.model;

import lombok.Generated;

import java.time.LocalDateTime;


public class SuccessResponse {

	private Integer responseCode;
	private String message;
	private Object data;

	public SuccessResponse() {
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

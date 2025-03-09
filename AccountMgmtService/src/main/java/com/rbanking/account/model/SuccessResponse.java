package com.rbanking.account.model;

import lombok.Generated;

import java.time.LocalDateTime;

@Generated
public class SuccessResponse {

	private Integer responseCode;
	private Object data;
	private String message;

	public SuccessResponse() {
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

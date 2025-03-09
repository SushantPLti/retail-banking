package com.rbanking.authserver.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 446278999619815472L;

	private String accessToken;
	private String tokenType;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}


}

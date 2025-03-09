package com.transaction.dto;

import jakarta.validation.constraints.NotNull;

public class AccStatementDto {
	
	
	@NotNull(message = "Please provide Account Number")
	private Long accountNumber;

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

}

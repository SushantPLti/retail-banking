package com.rbanking.account.DTO;

import java.io.Serializable;

public class TransactionDto implements Serializable {

	private static final long serialVersionUID = -1855705756758072415L;
	private Long accountNumber;
	private String transactionType;
	private Double amount;

	public TransactionDto(Long accountNumber, String transactionType, Double balance) {
		this.accountNumber = accountNumber;
		this.transactionType = transactionType;
		this.amount = balance;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}

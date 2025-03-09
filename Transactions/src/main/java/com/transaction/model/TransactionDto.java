package com.transaction.model;

import jakarta.validation.constraints.NotNull;

public class TransactionDto {
	
	@NotNull(message = "Please provide Account Number")
	private Long accountNumber;
	
	@NotNull(message = "Please provide Transaction Type")
	private TransactionType transactionType;

	@NotNull(message = "Amount can not be NULL")
	private Double amount;
	
	private Long otherAccountNumber;
	
	public Long getOtherAccountNumber() {
		return otherAccountNumber;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAccountNumber(long l) {
		// TODO Auto-generated method stub
		this.accountNumber = l;
	}
	public void setAmount(double d) {
		// TODO Auto-generated method stub
		this.amount = d;
	}
	public void setTransactionType(TransactionType credit) {
		// TODO Auto-generated method stub
		this.transactionType = credit;
	}
	
	
}

package com.transaction.dto;

public class FundTransferDto {

	
	private Long senderAccount;
	private Long receiverAccount;
	private Double amount;
	
	public Long getSenderAccount() {
		return senderAccount;
	}
	public void setSenderAccount(Long senderAccount) {
		this.senderAccount = senderAccount;
	}
	public Long getReceiverAccount() {
		return receiverAccount;
	}
	public void setReceiverAccount(Long receiverAccount) {
		this.receiverAccount = receiverAccount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}

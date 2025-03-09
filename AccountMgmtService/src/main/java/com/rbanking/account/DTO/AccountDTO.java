package com.rbanking.account.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.rbanking.account.entities.AccountType;
import com.rbanking.account.entities.Currency;
import com.rbanking.account.entities.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;

@Generated
public class AccountDTO implements Serializable {

	public AccountDTO(Long accountNumber, AccountType accountType,
			Long custId,
			Double balance,
			Status status,
			LocalDateTime createdAt,
			LocalDateTime updatedAt,
			String createdBy,
			String updatedBy,
					  Currency currency,
					  Double minBalance) {
		super();
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.custId = custId;
		this.balance = balance;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.currency = currency;
		this.minBalance = minBalance;
	}

	public AccountDTO() {
	}

	private static final long serialVersionUID = 99543170192176980L;

	private Long accountNumber;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Please provide account type!!")
	private AccountType accountType;

	@NotNull(message = "custId cannot be null")
	private Long custId;

	@NotNull(message = "Please provide balance")
	private Double balance;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Please provide status!!")
	private Status status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	@Enumerated(EnumType.STRING)
	private Currency currency;

	private Double minBalance;

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}
}

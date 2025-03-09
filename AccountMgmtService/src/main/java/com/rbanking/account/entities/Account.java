package com.rbanking.account.entities;

import jakarta.persistence.*;
import lombok.Generated;

import java.time.LocalDateTime;

@Entity
@Generated
public class Account {

    public Account() {
        super();
    }

    public Account(Long accountNumber, AccountType accountType, Long custId, Double balance, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Currency currency, Double minBalance) {
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSeqGen")
    @SequenceGenerator(name = "accountSeqGen", sequenceName = "account_seq", allocationSize = 1,  initialValue = 1341010027)
    private Long accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Long custId;

    private Double balance;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Double minBalance;
    
    private LocalDateTime lastTransactionDate;
    
    @Enumerated(EnumType.STRING)
    private FreezeRemark updateStatusRemark;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

	public LocalDateTime getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public FreezeRemark getUpdateStatusRemark() {
		return updateStatusRemark;
	}

	public void setUpdateStatusRemark(FreezeRemark updateStatusRemark) {
		this.updateStatusRemark = updateStatusRemark;
	}
	
    
    
}

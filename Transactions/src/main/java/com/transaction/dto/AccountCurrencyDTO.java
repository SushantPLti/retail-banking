package com.transaction.dto;

import java.io.Serial;
import java.io.Serializable;

import com.transaction.model.AccountStatus;

public class AccountCurrencyDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountCurrencyDTO() {
    }
    public AccountCurrencyDTO(Double balance,
                            String currency, Double minBalance, AccountStatus status) {
        super();
        this.balance = balance;
        this.currency = currency;
        this.minBalance = minBalance;
        this.status = status;
    }

    private Double balance;

    private String currency;
    
    private Double minBalance;
    
    private AccountStatus status;

    public Double getMinBalance() {
		return minBalance;
	}
	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}
	public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
    
    
}

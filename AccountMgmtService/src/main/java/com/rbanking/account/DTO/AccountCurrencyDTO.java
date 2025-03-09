package com.rbanking.account.DTO;

import com.rbanking.account.entities.Currency;
import com.rbanking.account.entities.Status;

import java.io.Serial;
import java.io.Serializable;

public class AccountCurrencyDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountCurrencyDTO() {
    }
    public AccountCurrencyDTO(Double balance,
                            Currency currency, Double minBalance, Status status) {
        super();
        this.balance = balance;
        this.currency = currency;
        this.minBalance = minBalance;
        this.status = status;
    }

    private Double balance;

    private Currency currency;
    
	private Double minBalance;
	
	private Status status;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}

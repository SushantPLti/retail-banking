package com.rbanking.account.DTO;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Generated;

@Generated
public class UpdateMinimumBalanceRequest implements Serializable{

	private static final long serialVersionUID = 5646592355402804278L;

	@NotNull(message = "Please provide ageCategory")
	private String ageCategory;
	@NotNull(message = "Please provide minBalance")
	private Double minBalance;
	
	public String getAgeCategory() {
		return ageCategory;
	}
	public void setAgeCategory(String ageCategory) {
		this.ageCategory = ageCategory;
	}
	public Double getMinBalance() {
		return minBalance;
	}
	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}
	
	
}

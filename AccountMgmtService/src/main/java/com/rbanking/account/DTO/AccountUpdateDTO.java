package com.rbanking.account.DTO;

import java.io.Serializable;

import com.rbanking.account.entities.FreezeRemark;
import com.rbanking.account.entities.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;

@Generated
public class AccountUpdateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public AccountUpdateDTO() {
	}
	public AccountUpdateDTO(Long accountNumber,
							Status status,
							String updatedBy,
							FreezeRemark remark
							) {
		super();
		this.status = status;
		this.accountNumber = accountNumber;
		this.updatedBy = updatedBy;
		this.remark = remark;
	}

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Please provide status!!")
	private Status status;

	private Long accountNumber;

	private String updatedBy;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Please provide remark!!")
	private FreezeRemark remark;


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public FreezeRemark getRemark() {
		return remark;
	}
	public void setRemark(FreezeRemark remark) {
		this.remark = remark;
	}
	
	
}

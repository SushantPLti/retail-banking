package com.rbanking.cservice.customerDTO;

import com.rbanking.cservice.entities.AgeCategory;
import com.rbanking.cservice.entities.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


public class UpdateDTO {
	
	public UpdateDTO(String address,
					 String email,
					 Long contactNo, String updatedBy, AgeCategory ageCategory, CustomerStatus customerStatus, String name, LocalDateTime updatedAt) {
		super();
		this.address = address;
		this.email = email;
		this.contactNo = contactNo;
		this.updatedBy = updatedBy;
		this.ageCategory = ageCategory;
		this.customerStatus = customerStatus;
		this.name = name;
		this.updatedAt = updatedAt;
	}

	public UpdateDTO() {
	}

	@NotBlank(message = "Please provide customer address!!")
    private String address;

	@NotBlank(message = "Please provide customer email!!")
	@Email
	private String email;
	
	@NotNull(message = "Please provide customer contact number!!")
	private Long contactNo;

	private String updatedBy;

	private AgeCategory ageCategory;

	private CustomerStatus customerStatus;

	private  String name;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}

	public CustomerStatus getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}

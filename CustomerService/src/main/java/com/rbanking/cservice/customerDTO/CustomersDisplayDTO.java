package com.rbanking.cservice.customerDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.rbanking.cservice.entities.AgeCategory;
import com.rbanking.cservice.entities.CustomerStatus;
import com.rbanking.cservice.entities.Role;
import lombok.Generated;


public class CustomersDisplayDTO {

	private Long custId;

	private String name;

	private String address;

	private LocalDate dob;

	private String email;

	private Long contactNo;

	private String panNumber;

	private Long aadhaarNumber;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private AgeCategory ageCategory;

	private CustomerStatus customerStatus;

	public CustomersDisplayDTO(Long custId, String name, String address, LocalDate dob, String email, Long contactNo,
			String panNumber, Long aadhaarNumber, LocalDateTime createdAt, LocalDateTime updatedAt,
			AgeCategory ageCategory, CustomerStatus customerStatus) {
		super();
		this.custId = custId;
		this.name = name;
		this.address = address;
		this.dob = dob;
		this.email = email;
		this.contactNo = contactNo;
		this.panNumber = panNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.ageCategory = ageCategory;
		this.customerStatus = customerStatus;
	}
	
	public CustomersDisplayDTO(){
		
	}
	
	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
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

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Long getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(Long aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
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
}

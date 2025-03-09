package com.rbanking.cservice.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSeqGen")
	@SequenceGenerator(name = "customerSeqGen", sequenceName = "customer_seq", allocationSize = 1,  initialValue = 11250236)
	private Long custId;
	private String name;
	private String address;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;
	@Email
	private String email;
	private Long contactNo;
	private String panNumber;
	private Long aadhaarNumber;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Please provide age category type!!")
	private AgeCategory ageCategory;
	@Enumerated(EnumType.STRING)
	private CustomerStatus customerStatus;
	private String createdBy;
	private String updatedBy;

	public Customer(Long custId, String name,
			String address,
			LocalDate dob,
			String email,
			Long contactNo,
			String panNumber, Long aadhaarNumber,
			LocalDateTime createdAt, LocalDateTime updatedAt, AgeCategory ageCategory, CustomerStatus customerStatus, String createdBy, String updatedBy) {
		this.custId = custId;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.contactNo = contactNo;
		this.createdAt = createdAt;
		this.dob = dob;
		this.email = email;
		this.updatedAt = updatedAt;
		this.name = name;
		this.panNumber = panNumber;
		this.ageCategory = ageCategory;
		this.customerStatus = customerStatus;
		this.createdBy=createdBy;
		this.updatedBy=updatedBy;
	}

	public Customer() {
		super();
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
}

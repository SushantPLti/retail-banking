package com.rbanking.cservice.customerDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbanking.cservice.entities.AgeCategory;
import com.rbanking.cservice.entities.CustomerStatus;
import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.validation.ValidPassword;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class CustomerDTO {

    public CustomerDTO() {
    }

    private Long custId;

    @NotBlank(message = "Please provide customer name!!")
    private String name;

    @NotBlank(message = "Please provide customer address!!")
    private String address;

    @NotNull(message = "Please provide customer date of birth!!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotBlank(message = "Please provide customer email!!")
    @Email
    private String email;

    @NotNull(message = "Please provide customer contact number!!")
    private Long contactNo;

    @NotNull(message = "Please provide customer Pan Number!")
    private String panNumber;

    @NotNull(message = "Please provide customer Aadhar Number!")
    private Long aadhaarNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//     @ValidPassword(message = "Password must be at least 8 character long and contains at least "
//     + "one digit, one lower case, one upper case and no whitespace")
//    @NotNull(message = "Please provide password!!")
    @ValidPassword
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please provide role type!!")
    private Role role;

    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please provide customer status!!")
    private CustomerStatus customerStatus;

    private String createdBy;
    private String updatedBy;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

package com.rbanking.cservice.util;

import com.rbanking.cservice.customerDTO.CustomerDTO;
import com.rbanking.cservice.customerDTO.CustomersDisplayDTO;
import com.rbanking.cservice.entities.Customer;

public class CustomerWrapper {

	public static Customer toCustomerEntity(CustomerDTO customerDto) {
		return new Customer(customerDto.getCustId(), customerDto.getName(),
				customerDto.getAddress(),
				customerDto.getDob(), customerDto.getEmail(),
				customerDto.getContactNo(), customerDto.getPanNumber(),
				customerDto.getAadhaarNumber(), customerDto.getCreatedAt(),
				customerDto.getUpdatedAt(),
				customerDto.getAgeCategory(),
				customerDto.getCustomerStatus(),
				customerDto.getCreatedBy(),
				customerDto.getUpdatedBy());

	}
	
	public static CustomersDisplayDTO toCustomerDetails(Customer customer) {
		return new CustomersDisplayDTO(customer.getCustId(), customer.getName(), customer.getAddress(), customer.getDob(),
				customer.getEmail(), customer.getContactNo(), customer.getPanNumber(), customer.getAadhaarNumber(),
				customer.getCreatedAt(), customer.getUpdatedAt(),customer.getAgeCategory(), customer.getCustomerStatus());
	}

}

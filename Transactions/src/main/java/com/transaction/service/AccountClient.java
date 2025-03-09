package com.transaction.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.transaction.dto.AccountCurrencyDTO;

import jakarta.validation.Valid;

@Service
@FeignClient(name = "AccountMgmtService")
public interface AccountClient {
	
	@PutMapping("/accounts/balance/{accountNumber}")
	String updateBalance(@PathVariable @Valid Long accountNumber, @RequestParam("balance") Double amount);
	
	@GetMapping("/accounts/balance/{accountNumber}")
	AccountCurrencyDTO getBalance(@PathVariable @Valid Long accountNumber);
	
	@GetMapping("/accounts/getCustomerEmail/{accountNo}")
	String getCustomerDetails(@PathVariable @Valid Long accountNo);

}

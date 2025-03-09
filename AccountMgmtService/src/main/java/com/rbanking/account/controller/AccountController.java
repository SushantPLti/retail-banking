package com.rbanking.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbanking.account.DTO.AccountCurrencyDTO;
import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.DTO.AccountUpdateDTO;
import com.rbanking.account.DTO.UpdateMinimumBalanceRequest;
import com.rbanking.account.entities.Account;
import com.rbanking.account.model.SuccessResponse;
import com.rbanking.account.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/*
 * Controller for handling account-related requests.
 * @author Saurav Mishra
 * 
 */
@RestController
@RequestMapping("/accounts")
@RefreshScope
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private AccountService accountService;

	/**
	 * Constructor for AccountController.
	 *
	 * @param accountService the account service to be used
	 */
	public AccountController(final AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Creates a new account.
	 *
	 * @param account the account to be created
	 * @return the response entity containing the success response and HTTP status
	 *         code
	 */
    @Operation(summary = "Create a new account")
	@PostMapping
	public ResponseEntity<SuccessResponse> createAccount(@Valid @RequestBody AccountDTO account) {
		return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
	}

	/**
	 * Retrieves an account by account Number.
	 *
	 * @param accountNo the accountNo of the account to be retrieved
	 * @return the response entity containing the account and HTTP status code
	 */
    @Operation(summary = "Retrieve an account by account number")
	@GetMapping("/{accountNo}")
	public ResponseEntity<Account> getAccount(@PathVariable Long accountNo) {
		return accountService.getAccount(accountNo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Updates the balance of an account.
	 *
	 * @param accountNo the account number
	 * @param balance   the new balance
	 * @return the response entity containing the success message and HTTP status
	 *         code
	 */
    @Operation(summary = "Update the balance of an account")
	@PutMapping("/balance/{accountNo}")
	public ResponseEntity<String> updateBalance(@PathVariable Long accountNo, @RequestParam Double balance) {
		logger.info("acc {} bal {} ", accountNo, balance);
		return new ResponseEntity<>(accountService.updateAccountBal(accountNo, balance), HttpStatus.OK);
	}

	/**
	 * Retrieves the balance of an account.
	 *
	 * @param accountNo the account number
	 * @return the response entity containing the account balance and HTTP status
	 *         code
	 */
    @Operation(summary = "Retrieve the balance of an account")
	@GetMapping("/balance/{accountNo}")
	public ResponseEntity<AccountCurrencyDTO> getBalance(@PathVariable Long accountNo) {
		return new ResponseEntity<>(accountService.viewBalance(accountNo), HttpStatus.OK);
	}

	/**
	 * Deletes an account by ID.
	 *
	 * @param id the ID of the account to be deleted
	 * @return the response entity containing the success response and HTTP status
	 *         code
	 */
    @Operation(summary = "Delete an account by ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse> deleteAcccount(@PathVariable Long id) {
		return new ResponseEntity<>(accountService.deleteAccount(id), HttpStatus.ACCEPTED);
	}

	/**
	 * Updates the status of an account.
	 *
	 * @param account the account with updated status
	 * @return the response entity containing the success response and HTTP status
	 *         code
	 */
    @Operation(summary = "Update the status of an account")
	@PutMapping("/updatestatus")
	public ResponseEntity<SuccessResponse> updateStatus(@Valid @RequestBody AccountUpdateDTO account,
			HttpServletRequest request) {

		return new ResponseEntity<>(accountService.updateAccountStatus(account, request), HttpStatus.OK);
	}

	/**
	 * Updates the minimum balance for the specified age category.
	 *
	 * @param updateMinBalReq the request containing the age category and the new minimum balance
	 * @return a ResponseEntity containing the success response with an HTTP status code
	 */
    @Operation(summary = "Update the minimum balance for a specified age category")
	@PutMapping("/updateMinBalance")
	public ResponseEntity<SuccessResponse> updateMinBalance(@Valid @RequestBody UpdateMinimumBalanceRequest updateMinBalReq) {
		SuccessResponse response = accountService.updateMinBalance(updateMinBalReq);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    /**
     * Controller method to get account numbers for a given customer.
     *
     * @param custId the ID of the customer whose account numbers are to be retrieved
     * @return a ResponseEntity containing a SuccessResponse with the list of account numbers
     */
    @Operation(summary = "Get account numbers", description = "Retrieve account numbers for the specified customer ID")
    @GetMapping("/getAccountNumbers/{custId}")
    public  ResponseEntity<SuccessResponse> getAccountNumbers(@PathVariable Long custId){
    	SuccessResponse response = accountService.getAccountNumbers(custId);
    	return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Controller method to get Email address of customer of given account.
     *
     * @param accountNo 
     * @return a ResponseEntity containing a SuccessResponse with the list of account numbers
     */
    @Operation(summary = "Get account numbers", description = "Retrieve account numbers for the specified customer ID")
    @GetMapping("/getCustomerEmail/{accountNo}")
    public  ResponseEntity<String> getCustomerDetails(@PathVariable Long accountNo){
    	String response = accountService.getCustomerDetailsByAccount(accountNo);
    	return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

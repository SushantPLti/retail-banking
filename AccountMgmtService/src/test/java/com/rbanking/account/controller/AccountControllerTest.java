package com.rbanking.account.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.rbanking.account.entities.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rbanking.account.DTO.AccountCurrencyDTO;
import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.DTO.AccountUpdateDTO;
import com.rbanking.account.entities.Account;
import com.rbanking.account.model.SuccessResponse;
import com.rbanking.account.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Mock
	private AccountService accountService; // Mock the account service

	@InjectMocks
	private AccountController accountController; // Inject the mocks into account controller

	@Mock
	private HttpServletRequest request;

	private AccountDTO accountDTO;
	private AccountUpdateDTO accountUpdateDTO;
	private Account account;
	private SuccessResponse successResponse;
	private AccountCurrencyDTO accountCurrencyDTO;

	@BeforeEach
	void setUp() {
		// Initialize the test objects
		accountDTO = new AccountDTO();
		accountUpdateDTO = new AccountUpdateDTO();
		account = new Account();
		successResponse = new SuccessResponse();
		accountCurrencyDTO = new AccountCurrencyDTO();
	}

	@Test
	void testCreateAccount() { // Test the createAccount method
		// Mock the account service's createAccount method to return the success
		// response
		when(accountService.createAccount(any(AccountDTO.class))).thenReturn(successResponse);

		// Call the createAccount method on the account controller
		ResponseEntity<SuccessResponse> response = accountController.createAccount(accountDTO);

		// Assert that the response status code is CREATED
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		// Assert that the response body is not null
		assertNotNull(response.getBody());
		// Assert that the response body is the success response
		assertEquals(successResponse, response.getBody());
	}

	@Test
	void testGetAccount() { // Test the getAccount method
		// Mock the account service's getAccount method to return an optional containing
		// an account
		when(accountService.getAccount(eq(1L))).thenReturn(Optional.of(account));

		// Call the getAccount method on the account controller with account number 1
		ResponseEntity<Account> response = accountController.getAccount(1L);

		// Assert that the response status code is OK
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Assert that the response body is not null
		assertNotNull(response.getBody());
		// Assert that the response body is the account
		assertEquals(account, response.getBody());
	}

	@Test
	void testGetAccount_NotFound() { // Test the getAccount method when the account is not found
		// Mock the account service's getAccount method to return an empty optional
		when(accountService.getAccount(eq(1L))).thenReturn(Optional.empty());

		// Call the getAccount method on the account controller with account number 1
		ResponseEntity<Account> response = accountController.getAccount(1L);

		// Assert that the response status code is NOT_FOUND
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testUpdateBalance() { // Test the updateBalance method
		// Mock the account service's updateAccountBal method to return "Success"
		when(accountService.updateAccountBal(eq(1L), eq(1000.0))).thenReturn("Success");

		// Call the updateBalance method on the account controller with account number 1
		// and balance 1000.0
		ResponseEntity<String> response = accountController.updateBalance(1L, 1000.0);

		// Assert that the response status code is OK
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Assert that the response body is "Success"
		assertEquals("Success", response.getBody());
		assertNotNull(response.getBody());
		// Verify that the updateAccountBal method on the account service was called
		// once with account number 1 and balance 1000.0
		verify(accountService, times(1)).updateAccountBal(1L, 1000.0);
	}

	@Test
	void testGetBalance() { // Test the getBalance method
		// Mock the account service's viewBalance method to return accountCurrencyDTO
		when(accountService.viewBalance(eq(1L))).thenReturn(accountCurrencyDTO);

		// Call the getBalance method on the account controller with account number 1
		ResponseEntity<AccountCurrencyDTO> response = accountController.getBalance(1L);

		// Assert that the response status code is OK
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Assert that the response body is not null
		assertNotNull(response.getBody());
		// Assert that the response body is accountCurrencyDTO
		assertEquals(accountCurrencyDTO, response.getBody());
	}

	@Test
	void testDeleteAccount() { // Test the deleteAccount method
		// Mock the account service's deleteAccount method to return the success
		// response
		when(accountService.deleteAccount(eq(1L))).thenReturn(successResponse);

		// Call the deleteAccount method on the account controller with ID 1
		ResponseEntity<SuccessResponse> response = accountController.deleteAcccount(1L);

		// Assert that the response status code is NO_CONTENT
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		// Assert that the response body is not null
		assertNotNull(response.getBody());
		// Assert that the response body is the success response
		assertEquals(successResponse, response.getBody());
	}

	@Test
	public void testUpdateStatus() {
		AccountUpdateDTO updateDTO = new AccountUpdateDTO();
		updateDTO.setAccountNumber(123456L);
		updateDTO.setStatus(Status.INACTIVE);
		updateDTO.setUpdatedBy("Admin");

		SuccessResponse successResponse = new SuccessResponse();
		successResponse.setResponseCode(HttpStatus.OK.value());
		// Add additional fields to successResponse as needed

		when(accountService.updateAccountStatus(any(AccountUpdateDTO.class), any(HttpServletRequest.class)))
				.thenReturn(successResponse);

		ResponseEntity<SuccessResponse> responseEntity = accountController.updateStatus(updateDTO, request);

		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(successResponse, responseEntity.getBody());

		verify(accountService, times(1)).updateAccountStatus(any(AccountUpdateDTO.class), any(HttpServletRequest.class));
	}

	@Test
	void testGetAccountNumbers() { // Test the getAccountNumbers method
		Long custId = 1L;
		SuccessResponse mockResponse = new SuccessResponse();

		// Mock the account service's getAccountNumbers method to return the success response
		when(accountService.getAccountNumbers(custId)).thenReturn(mockResponse);

		// Call the getAccountNumbers method on the account controller with custId
		ResponseEntity<SuccessResponse> response = accountController.getAccountNumbers(custId);

		// Assert that the response status code is OK
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Assert that the response body is not null
		assertNotNull(response.getBody());
		// Assert that the response body is the mock response
		assertEquals(mockResponse, response.getBody());

		// Verify that the getAccountNumbers method on the account service was called once with custId
		verify(accountService, times(1)).getAccountNumbers(custId);
	}



}

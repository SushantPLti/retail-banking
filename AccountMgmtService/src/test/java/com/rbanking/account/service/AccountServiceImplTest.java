package com.rbanking.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.rbanking.account.util.Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.rbanking.account.DTO.AccountCurrencyDTO;
import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.DTO.AccountUpdateDTO;
import com.rbanking.account.entities.Account;
import com.rbanking.account.entities.Currency;
import com.rbanking.account.entities.Status;
import com.rbanking.account.exception.AccountNotFoundException;
import com.rbanking.account.feign.CustomersClient;
import com.rbanking.account.feign.TransactionsClient;
import com.rbanking.account.model.SuccessResponse;
import com.rbanking.account.repo.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
	private AccountRepository accountRepo;

	@Mock
	private TransactionsClient transactionsClient;

	@Mock
	private CustomersClient customersClient;

	@Mock
	private JwtService jwtService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private SuccessResponse successResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateAccount() {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setCustId(123L);
		accountDTO.setBalance(5000.0);

		Account account = new Account();
		account.setAccountNumber(123456L);
		account.setBalance(5000.0);
		account.setMinBalance(1000.0);
		account.setCreatedAt(LocalDateTime.now());
		account.setUpdatedAt(LocalDateTime.now());

		when(customersClient.ageCategory(any(Long.class))).thenReturn("REGULAR");
		when(accountRepo.save(any(Account.class))).thenReturn(account);

		SuccessResponse response = accountService.createAccount(accountDTO);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED.value(), response.getResponseCode());
	}

	@Test
	void testGetAccount() {
		Account account = new Account();
		account.setAccountNumber(123456L);

		when(accountRepo.findByAccountNumber(any())).thenReturn(Optional.of(account));

		Optional<Account> result = accountService.getAccount(123456L);

		assertTrue(result.isPresent());
		assertEquals(123456L, result.get().getAccountNumber());
	}

	@Test
	void testUpdateAccountBal() {
		Account account = new Account();
		account.setAccountNumber(123456L);
		account.setBalance(5000.0);

		when(accountRepo.findByAccountNumber(any())).thenReturn(Optional.of(account));
		when(accountRepo.save(any())).thenReturn(account);

		String response = accountService.updateAccountBal(123456L, 10000.0);

		assertEquals("Your account has been successfully updated with the 10000.0 amount", response);
	}

	@Test
	void testViewBalance() {
		Account account = new Account();
		account.setAccountNumber(123456L);
		account.setBalance(5000.0);
		account.setCurrency(Currency.INR);

		when(accountRepo.findByAccountNumber(any())).thenReturn(Optional.of(account));

		AccountCurrencyDTO result = accountService.viewBalance(123456L);

		assertNotNull(result);
		assertEquals(5000.0, result.getBalance());
		assertEquals(Currency.INR, result.getCurrency());
	}

	@Test
	void testDeleteAccount() {
		doNothing().when(accountRepo).deleteById(any());

		SuccessResponse response = accountService.deleteAccount(123456L);

		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
	}

	@Test
	public void testUpdateAccountStatus() {
		Account account = new Account();
		account.setAccountNumber(123456L);
		account.setStatus(Status.ACTIVE);

		AccountUpdateDTO updateDTO = new AccountUpdateDTO();
		updateDTO.setAccountNumber(123456L);
		updateDTO.setStatus(Status.INACTIVE);
		updateDTO.setUpdatedBy("OPERATOR");

		String bearerToken = "mockToken";
		String role = "ROLE_USER";

		when(request.getHeader(Constant.AUTHORIZATION)).thenReturn("Bearer " + bearerToken);
		when(jwtService.extractRoles(bearerToken)).thenReturn(Collections.singletonList(role));
		when(accountRepo.findByAccountNumber(anyLong())).thenReturn(Optional.of(account));
		when(accountRepo.save(any(Account.class))).thenReturn(account);

		// Call the real method
		SuccessResponse response = accountService.updateAccountStatus(updateDTO, request);

		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());

		// Verify internal method calls
		verify(request, times(1)).getHeader(Constant.AUTHORIZATION);
		verify(jwtService, times(1)).extractRoles(bearerToken);
		verify(accountRepo, times(1)).findByAccountNumber(anyLong());
		verify(accountRepo, times(1)).save(any(Account.class));
	}


	@Test
	void testGetAccountNotFound() {
		when(accountRepo.findByAccountNumber(any())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(123456L));
	}

	@Test
	void testGetAccountNumbers() {
		MockitoAnnotations.openMocks(this);

		Long customerId = 1L;
		Account account1 = new Account();
		account1.setAccountNumber(123456L);
		Account account2 = new Account();
		account2.setAccountNumber(789012L);

		List<Account> accountList = Arrays.asList(account1, account2);

		// Mock the account repository's findByCustId method to return the optional list of accounts
		when(accountRepo.findByCustId(customerId)).thenReturn(Optional.of(accountList));

		// Call the getAccountNumbers method on the account service with customerId
		SuccessResponse response = accountService.getAccountNumbers(customerId);

		// Assert that the response is not null
		assertNotNull(response);
		// Assert that the response code is OK
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		// Assert that the message is as expected
		assertEquals("Numbers of accounts with cust id " + customerId + " is " + accountList.size(), response.getMessage());
		// Assert that the data is the list of account numbers
		assertEquals(Arrays.asList(123456L, 789012L), response.getData());

		// Verify that the findByCustId method on the account repository was called once with customerId
		verify(accountRepo, times(1)).findByCustId(customerId);
	}


}

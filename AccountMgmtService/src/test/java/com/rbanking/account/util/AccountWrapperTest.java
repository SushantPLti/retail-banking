package com.rbanking.account.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.entities.Account;
import com.rbanking.account.entities.AccountType;
import com.rbanking.account.entities.Currency;
import com.rbanking.account.entities.Status;

class AccountWrapperTest {

	@Test
	public void testToAccountDTO() {
		Account account = new Account(123456789L, AccountType.SAVINGS, 1001L, 5000.0, Status.ACTIVE,
				LocalDateTime.now(), LocalDateTime.now(), "Admin", "Admin",Currency.INR, 500.0);
		AccountDTO accountDTO = AccountWrapper.toAccountDTO(account);

		assertEquals(account.getAccountNumber(), accountDTO.getAccountNumber());
		assertEquals(account.getAccountType(), accountDTO.getAccountType());
		assertEquals(account.getCustId(), accountDTO.getCustId());
		assertEquals(account.getBalance(), accountDTO.getBalance());
		assertEquals(account.getStatus(), accountDTO.getStatus());
		assertEquals(account.getCreatedAt(), accountDTO.getCreatedAt());
		assertEquals(account.getUpdatedAt(), accountDTO.getUpdatedAt());
		assertEquals(account.getCreatedBy(), accountDTO.getCreatedBy());
		assertEquals(account.getUpdatedBy(), accountDTO.getUpdatedBy());
	}

	@Test
	public void testToAccountEntity() {
		AccountDTO accountDTO = new AccountDTO(123456789L, AccountType.SAVINGS, 1001L, 5000.0, Status.ACTIVE,
				LocalDateTime.now(), LocalDateTime.now(), "Admin", "Admin", Currency.INR, 500.0);
		Account account = AccountWrapper.toAccountEntity(accountDTO);

		assertEquals(accountDTO.getAccountNumber(), account.getAccountNumber());
		assertEquals(accountDTO.getAccountType(), account.getAccountType());
		assertEquals(accountDTO.getCustId(), account.getCustId());
		assertEquals(accountDTO.getBalance(), account.getBalance());
		assertEquals(accountDTO.getStatus(), account.getStatus());
		assertEquals(accountDTO.getCreatedAt(), account.getCreatedAt());
		assertEquals(accountDTO.getUpdatedAt(), account.getUpdatedAt());
		assertEquals(accountDTO.getCreatedBy(), account.getCreatedBy());
		assertEquals(accountDTO.getUpdatedBy(), account.getUpdatedBy());
	}
}

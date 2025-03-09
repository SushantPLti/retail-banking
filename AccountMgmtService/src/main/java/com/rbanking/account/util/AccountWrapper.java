package com.rbanking.account.util;

import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.entities.Account;

/**
 * The AccountWrapper utility class provides methods to convert between Account
 * entity and AccountDTO.
 */
public class AccountWrapper {

	/**
	 * Converts an Account entity to an AccountDTO.
	 *
	 * @param account the Account entity to convert
	 * @return the converted AccountDTO
	 */
	public static AccountDTO toAccountDTO(Account account) {
		return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getCustId(),
				account.getBalance(), account.getStatus(), account.getCreatedAt(),
				account.getUpdatedAt(), account.getCreatedBy(), account.getUpdatedBy(), account.getCurrency(), account.getMinBalance());

	}

	/**
	 * Converts an AccountDTO to an Account entity.
	 *
	 * @param accountDTO the AccountDTO to convert
	 * @return the converted Account entity
	 */
	public static Account toAccountEntity(AccountDTO accountDTO) {
		return new Account(accountDTO.getAccountNumber(), accountDTO.getAccountType(), accountDTO.getCustId(),
				accountDTO.getBalance(), accountDTO.getStatus(),
				accountDTO.getCreatedAt(), accountDTO.getUpdatedAt(), accountDTO.getCreatedBy(), accountDTO.getUpdatedBy(), accountDTO.getCurrency(),accountDTO.getMinBalance());

	}
}

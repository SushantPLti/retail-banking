package com.rbanking.account.service;

import java.util.Optional;

import com.rbanking.account.DTO.AccountCurrencyDTO;
import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.DTO.AccountUpdateDTO;
import com.rbanking.account.DTO.UpdateMinimumBalanceRequest;
import com.rbanking.account.entities.Account;
import com.rbanking.account.entities.FreezeRemark;
import com.rbanking.account.model.SuccessResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * AccountService interface provides methods to manage accounts. This includes
 * creating, retrieving, updating, viewing balance, deleting accounts, and
 * updating account status.
 */
public interface AccountService {

	/**
	 * Creates a new account.
	 *
	 * @param account the account entity to be created
	 * @return a response indicating the success of the operation
	 */
	SuccessResponse createAccount(AccountDTO account);

	/**
	 * Retrieves an account by its accountNo.
	 *
	 * @param accountNo is the account number of the account
	 * @return an optional containing the account if found, empty otherwise
	 */
	Optional<Account> getAccount(Long accountNo);

    /**
     * Updates the balance of an account.
     *
     * @param accountNo the account number
     * @param balance the new balance
     * @return a message indicating the success of the operation
     */
	String updateAccountBal(Long accountNo, Double balance);

    /**
     * Views the balance of an account.
     *
     * @param accountNo the account number
     * @return the balance of the account
     */
	AccountCurrencyDTO viewBalance(Long accountNo);

    /**
     * Deletes an account by its account number.
     *
     * @param accountNo the account number
     * @return a response indicating the success of the operation
     */
	SuccessResponse deleteAccount(Long accountNo);

    /**
     * Updates the status of an account.
     *
     * @param account the account entity with updated status
     * @return a response indicating the success of the operation
     */
	SuccessResponse updateAccountStatus(AccountUpdateDTO account,HttpServletRequest request);

	/**
	 * Updates the minimum balance for the specified age category.
	 *
	 * @param updateMinBalReq the request containing the age category and the new minimum balance
	 * @return a SuccessResponse containing the response code and message indicating the result of the update operation
	 */
	SuccessResponse updateMinBalance(UpdateMinimumBalanceRequest updateMinBalReq);


	/**
	 * Retrieves account numbers associated with a given customer ID.
	 *
	 * @param customerId the ID of the customer whose account numbers are to be retrieved
	 * @return a SuccessResponse containing the response code, a message, and a list of account numbers
	 */
	SuccessResponse getAccountNumbers(Long customerId);
	boolean isAccountInactive(Account account);

	void updateAccountStatus(Account account);

	String getCustomerDetailsByAccount(Long custId);


}

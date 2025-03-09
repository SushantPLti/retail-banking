package com.transaction.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transaction.dto.TransactionResponse;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionDto;

@Service
public interface TransactionService {

	/**
	 * Performs a new transaction.
	 *
	 * @param  accountNumber, amount, TransactionType type (credit/withdrawn)
	 * @return transaction details
	 */
	TransactionResponse<Transaction> performTransaction(TransactionDto transaction) throws Exception;


	/**
	 * Fetches all the transaction for a particular account
	 *
	 * @param accountNumber
	 * @param pageable
	 * @return List of all transaction for a particular account
	 */
	List<Transaction> getAllTransactions(Long accountNumber, Pageable pageable);


	/**
	 * Fetches all the transaction for a particular account
	 *
	 * @param  accountNumber
	 * @return List of all transaction for a particular account
	 */
	List<Transaction> getTransaction(Long accountNumber);
	
	/**
	 * Performs a  fund transfer.
	 *
	 * @param  senderAccount, amount, receiverAccount 
	 * @return void
	 */
	Transaction transferFunds(Long senderAccount, Long receiverAccount, Double amount) throws Exception;

	List<Transaction> getPeriodTransaction(Long accountNumber, LocalDate fromDate, LocalDate toDate);
	List<Transaction> getLastTransaction(Long accountNumber,int days);
	List<Transaction> getQuarterlyTransaction(Long accountNumber);
}

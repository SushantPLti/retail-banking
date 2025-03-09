package com.transaction.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.transaction.dto.AccountCurrencyDTO;
import com.transaction.dto.TransactionResponse;
import com.transaction.exception.AccountNotFoundException;
import com.transaction.exception.FreezeAccountException;
import com.transaction.exception.InsufficientBalanceException;
import com.transaction.exception.MinBalanceLimitCrossException;
import com.transaction.exception.NoTransactionsFoundException;
import com.transaction.model.AccountStatus;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionDto;
import com.transaction.model.TransactionType;
import com.transaction.notification.NotificationService;
import com.transaction.repository.TransactionRepository;
import com.transaction.util.CurrencyConverter;
import com.transaction.util.CustomDateTime;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class TransactioServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountClient accountclient;

	@Autowired
	private NotificationService notificationService;

//	@Autowired
//	private Notification notify;

	private static final Logger logger = LoggerFactory.getLogger(TransactioServiceImpl.class);

	private static final long STARTING_REFERENCE_NUMBER = 10000000000L;

	private int attempts = 1;

	private static final AtomicInteger counter = new AtomicInteger(1000);

	/**
	 * Performs a new transaction.
	 *
	 * @return transaction details
	 */
	@Override
	@CircuitBreaker(name = "TRANSACTIONSERVICE", fallbackMethod = "fallbackPerformTransaction")
	// @Retry(name = "TRANSACTIONSERVICE", fallbackMethod =
	// "fallbackPerformTransaction")
	public TransactionResponse<Transaction> performTransaction(TransactionDto trans) throws Exception {
		logger.info("Retry attempts: " + attempts + " at time " + new java.util.Date());
		Long accountNumber = trans.getAccountNumber();
		Double amount = trans.getAmount();
		TransactionType type = trans.getTransactionType().toString().equals(TransactionType.ACCOUNT_OPEN.toString())
				? TransactionType.CREDIT
				: trans.getTransactionType();
		Double updatedAmount = 0.0;

		if (!trans.getTransactionType().toString().equals(TransactionType.ACCOUNT_OPEN.toString())) {

			AccountCurrencyDTO accountObj = accountclient.getBalance(accountNumber);

			if (accountObj.getStatus() != AccountStatus.BLOCKED) {
				Double balance = accountObj.getBalance();
				Double minBalance = accountObj.getMinBalance();

				if (type == TransactionType.DEBIT && balance < amount) {
					throw new Exception("Insufficient Balance");
				}

				if (minBalance > (balance + amount))
					throw new MinBalanceLimitCrossException(
							"The transaction could not be completed because the sender's account balance is below the minimum required limit.");

				updatedAmount = (type == TransactionType.CREDIT) ? balance + amount : balance - amount;
				accountclient.updateBalance(accountNumber, updatedAmount);

			} else {
				throw new FreezeAccountException(
						"Your account has been frozen due to suspicious activity. Please contact support.");
			}

		} else
			updatedAmount = amount;

		// Update Via feign Client
		Transaction transaction = new Transaction();
		transaction.setAccountNumber(accountNumber);
		transaction.setTransactionAmount(amount);
		transaction.setTransactionType(type);
		transaction.setCreatedAt(CustomDateTime.getTimeSpecificZone());
		transaction.setClosingBalance(updatedAmount);

			Transaction transactionData = transactionRepository.save(transaction);
			Long referenceNumber = generateReferenceNumber();
			logger.info("Reference number: " + referenceNumber);
			transactionData.setReferenceNumber(referenceNumber);
			transactionRepository.save(transactionData);

			if (!trans.getTransactionType().toString().equals(TransactionType.ACCOUNT_OPEN.toString())) {
			// Send email notification to the sender
			String senderEmail = accountclient.getCustomerDetails(accountNumber);

			String typStr = transaction.getTransactionType().toString().toLowerCase()+ "ed";
			String senderEmailSubject = "Transaction Alert: Funds " + typStr;
			String senderEmailText = String.format(
					"Dear Customer,\n\n" + "Your account has been "+ typStr +" with an amount of %.2f.\n"
							+ "Transaction Details:\n" + "Reference ID: %d\n"
							+ typStr +" Amount: %.2f\n" + "Closing Balance: %.2f\n\n"
							+ "Thank you for banking with us.\n\n" + "Best Regards,\n" + "Your Bank",
					amount, referenceNumber, amount, transaction.getClosingBalance());
			logger.info("email " + senderEmail +"\n senderEmailSubject: " +"\nsenderEmailText " + senderEmailText);
			notificationService.sendEmail(senderEmail, senderEmailSubject, senderEmailText);
			}
			return new TransactionResponse<>(transactionData,
					transactionData.getTransactionAmount() + " amount is "
							+ trans.getTransactionType().toString().toLowerCase() + " into " + trans.getAccountNumber(),
					HttpStatus.CREATED);
		} 
//	else {
//			throw new FreezeAccountException(
//					"Your account has been frozen due to suspicious activity. Please contact support.");
//		}
//	}

	public TransactionResponse<Transaction> fallbackPerformTransaction(TransactionDto trans, FeignException t) {
		logger.error("Fallback method called due to: " + t.getMessage());
		return new TransactionResponse<>(null, "Account Service is currently unavailable. Please try again later. ",
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * Performs a new transaction.
	 *
	 * @param accountNumber
	 * @return all transaction for a particular account
	 */
	@Override
	public List<Transaction> getTransaction(Long accountNumber) {
		return transactionRepository.findByAccountNumberOrderByTransactionIdDesc(accountNumber);
	}

	/**
	 * Performs a new transaction.
	 *
	 * @param accountNumber
	 * @param pageable
	 * @return all transaction for a particular account
	 */
	@Override
	public List<Transaction> getAllTransactions(Long accountNumber, Pageable pageable) {
		Page<Transaction> page = transactionRepository.findAllTransactionsByAccountNumber(accountNumber, pageable);
		return page.getContent();
	}

	/**
	 * Performs a fund transfer.
	 *
	 * @param senderAccount, amount, receiverAccount
	 * @return void
	 */
	@Override
	@CircuitBreaker(name = "TRANSACTIONSERVICE", fallbackMethod = "fallbackTransferFunds")
	@Retry(name = "TRANSACTIONSERVICE", fallbackMethod = "fallbackTransferFunds")
	public Transaction transferFunds(Long senderAccount, Long receiverAccount, Double amount) throws Exception {
		AccountCurrencyDTO senderAccountObj = accountclient.getBalance(senderAccount);

		AccountCurrencyDTO receiverAccountObj = accountclient.getBalance(receiverAccount);

		if (senderAccountObj.getStatus() != AccountStatus.BLOCKED
				|| receiverAccountObj.getStatus() != AccountStatus.BLOCKED) {
			if (senderAccountObj == null || senderAccountObj.getBalance() == null) {
				throw new NullPointerException("The transaction couldn't take place because account balance was null");
			}
			Double senderBalance = senderAccountObj.getBalance();
			Double minBalance = senderAccountObj.getMinBalance();

			logger.debug("Sender balance {} " + senderBalance);

			if (senderBalance < amount) {
				throw new InsufficientBalanceException("Insufficient balance in sender's account");
			}

			if (minBalance > (senderBalance + amount)) {
				throw new MinBalanceLimitCrossException(
						"The transaction could not be completed because the sender's account balance is below the minimum required limit.");
			}

			logger.debug("Updated sender balance {} " + (senderBalance - amount));
			accountclient.updateBalance(senderAccount, (senderBalance - amount));

			if (receiverAccountObj == null || receiverAccountObj.getBalance() == null) {
				throw new NullPointerException(
						"The transaction couldn't take place because receiver account balance was null");
			}
			Double receiverBalance = receiverAccountObj.getBalance();

			String sourceCurrency = senderAccountObj.getCurrency();
			String receiverCurrency = receiverAccountObj.getCurrency();
			CurrencyConverter converter = new CurrencyConverter();

			Double convertedAmount = converter.convert(sourceCurrency, receiverCurrency, amount);
			accountclient.updateBalance(receiverAccount, (receiverBalance + convertedAmount));

			// Record Sender Transaction
			Transaction senderTransaction = new Transaction();
			senderTransaction.setAccountNumber(senderAccount);
			senderTransaction.setOtherAccountNumber(receiverAccount);
			senderTransaction.setTransactionAmount(amount);
			senderTransaction.setTransactionType(TransactionType.DEBIT);
			senderTransaction.setCreatedAt(CustomDateTime.getTimeSpecificZone());
			senderTransaction.setClosingBalance((senderBalance - amount));
			Transaction savesender = transactionRepository.save(senderTransaction);

			Long referenceNumber = generateReferenceNumber();
			logger.info("Reference number: " + referenceNumber);
			savesender.setReferenceNumber(referenceNumber);
			transactionRepository.save(savesender);
			// Record Receiver Transaction
			Transaction receiverTransaction = new Transaction();
			receiverTransaction.setAccountNumber(receiverAccount);
			receiverTransaction.setOtherAccountNumber(senderAccount);
			receiverTransaction.setTransactionAmount(convertedAmount);
			receiverTransaction.setTransactionType(TransactionType.CREDIT);
			receiverTransaction.setCreatedAt(CustomDateTime.getTimeSpecificZone());
			receiverTransaction.setClosingBalance(receiverBalance + convertedAmount);
			receiverTransaction.setReferenceNumber(referenceNumber);
			transactionRepository.save(receiverTransaction);

			// Send email notification
			String senderEmail = accountclient.getCustomerDetails(senderTransaction.getAccountNumber());
			String receiverEmail = accountclient.getCustomerDetails(receiverTransaction.getAccountNumber());
//            
			// Send email notification to the sender
			String senderEmailSubject = "Transaction Alert: Funds Debited";
			String senderEmailText = String.format(
					"Dear Customer,\n\n" + "Your account has been debited with an amount of %.2f.\n"
							+ "Transaction Details:\n" + "Reference ID: %d\n" + "Receiver Account Number: %d\n"
							+ "Debited Amount: %.2f\n" + "Closing Balance: %.2f\n\n"
							+ "Thank you for banking with us.\n\n" + "Best Regards,\n" + "Your Bank",
					amount, referenceNumber, receiverAccount, amount, senderTransaction.getClosingBalance());
			notificationService.sendEmail(senderEmail, senderEmailSubject, senderEmailText);

			// Send email notification to the receiver
			String receiverEmailSubject = "Transaction Alert: Funds Credited";
			String receiverEmailText = String.format(
					"Dear Customer,\n\n" + "Your account has been credited with an amount of %.2f.\n"
							+ "Transaction Details:\n" + "Reference ID: %d\n" + "Sender Account Number: %d\n"
							+ "Credited Amount: %.2f\n" + "Closing Balance: %.2f\n\n"
							+ "Thank you for banking with us.\n\n" + "Best Regards,\n" + "Your Bank",
					convertedAmount, referenceNumber, senderAccount, convertedAmount,
					receiverTransaction.getClosingBalance());
			notificationService.sendEmail(receiverEmail, receiverEmailSubject, receiverEmailText);
			return senderTransaction;
		} else {
			throw new FreezeAccountException(
					"Your account has been frozen due to suspicious activity. Please contact support.");
		}
	}

	public TransactionResponse<Transaction> fallbackTransferFunds(Long senderAccount, Long receiverAccount,
			Double amount, Throwable t) {
		logger.error("Fallback method called due to: " + t.getMessage());
		return new TransactionResponse<>(null, "Account Service is currently unavailable. Please try again later. ",
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@Override
	public List<Transaction> getPeriodTransaction(Long accountNumber, LocalDate fromDate, LocalDate toDate) {
		try {
			List<Transaction> unFilteredTransaction = getTransaction(accountNumber);
			logger.info("Unfilter: " + unFilteredTransaction);

			if (unFilteredTransaction == null || unFilteredTransaction.isEmpty()) {
				throw new AccountNotFoundException("No transactions found for account number: " + accountNumber);
			}

			List<Transaction> filteredTransactions = unFilteredTransaction.stream()
					.filter(t -> t.getAccountNumber().equals(accountNumber)
							&& !t.getCreatedAt().toLocalDate().isBefore(fromDate)
							&& !t.getCreatedAt().toLocalDate().isAfter(toDate))
					.collect(Collectors.toList());

			if (filteredTransactions.isEmpty()) {
				throw new NoTransactionsFoundException("No transactions found for the specified date range.");
			}

			return filteredTransactions;
		} catch (AccountNotFoundException | NoTransactionsFoundException e) {
			logger.error("Error: " + e.getMessage());
			throw e; // Re-throw the exception to be handled by the calling method or global
						// exception handler
		} catch (Exception e) {
			logger.error("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred while fetching transactions.", e);
		}
	}

	public static Long generateReferenceNumber() {
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
		long timestamp = zonedDateTime.toInstant().toEpochMilli();
		int uniqueNumber = counter.getAndIncrement();
		String referenceNumberStr = String.format("%d%04d", timestamp, uniqueNumber);
		return Long.parseLong(referenceNumberStr);
	}

	@Override
	public List<Transaction> getQuarterlyTransaction(Long accountNumber) {
		try {
			LocalDate now = LocalDate.now();
			LocalDate startOfQuarter;
			LocalDate endOfQuarter = now.withDayOfMonth(1).minusDays(1); // End of last month

			if (now.getMonthValue() <= 3) {
				startOfQuarter = LocalDate.of(now.getYear() - 1, 10, 1); // Last year's Q4
			} else if (now.getMonthValue() <= 6) {
				startOfQuarter = LocalDate.of(now.getYear(), 1, 1); // Q1
			} else if (now.getMonthValue() <= 9) {
				startOfQuarter = LocalDate.of(now.getYear(), 4, 1); // Q2
			} else {
				startOfQuarter = LocalDate.of(now.getYear(), 7, 1); // Q3
			}

			List<Transaction> unFilteredTransaction = getTransaction(accountNumber);
			logger.info("Unfiltered: " + unFilteredTransaction);

			if (unFilteredTransaction == null || unFilteredTransaction.isEmpty()) {
				throw new AccountNotFoundException("No transactions found for account number: " + accountNumber);
			}

			List<Transaction> filteredTransactions = unFilteredTransaction.stream()
					.filter(t -> t.getAccountNumber().equals(accountNumber)
							&& !t.getCreatedAt().toLocalDate().isBefore(startOfQuarter)
							&& !t.getCreatedAt().toLocalDate().isAfter(endOfQuarter))
					.collect(Collectors.toList());

			if (filteredTransactions.isEmpty()) {
				throw new NoTransactionsFoundException("No transactions found for the last quarter.");
			}

			return filteredTransactions;
		} catch (AccountNotFoundException | NoTransactionsFoundException e) {
			logger.error("Error: " + e.getMessage());
			throw new NoTransactionsFoundException("No transaction exists in the last quarter");
		} catch (Exception e) {
			logger.error("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred while fetching transactions.", e);
		}
	}

	@Override
	public List<Transaction> getLastTransaction(Long accountNumber, int days) {
		try {
			LocalDate endDate = LocalDate.now();
			LocalDate startDate = endDate.minusDays(days);

			List<Transaction> unFilteredTransaction = getTransaction(accountNumber);
			logger.info("Unfiltered: " + unFilteredTransaction);

			if (unFilteredTransaction == null || unFilteredTransaction.isEmpty()) {
				throw new AccountNotFoundException("No transactions found for account number: " + accountNumber);
			}

			List<Transaction> filteredTransactions = unFilteredTransaction.stream()
					.filter(t -> t.getAccountNumber().equals(accountNumber)
							&& !t.getCreatedAt().toLocalDate().isBefore(startDate)
							&& !t.getCreatedAt().toLocalDate().isAfter(endDate))
					.collect(Collectors.toList());

			if (filteredTransactions.isEmpty()) {
				throw new NoTransactionsFoundException("No transactions found for the last " + days + " days.");
			}

			return filteredTransactions;
		} catch (AccountNotFoundException | NoTransactionsFoundException e) {
			logger.error("Error: " + e.getMessage());
			throw new NoTransactionsFoundException("No transaction exists in the last " + days + " specified days"); // Re-throw
																														// the
																														// exception
																														// to
																														// be
																														// handled
																														// by
																														// the
																														// calling
																														// method
																														// or
																														// global
																														// exception
																														// handler
		} catch (Exception e) {
			logger.error("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred while fetching transactions.", e);
		}
	}
}
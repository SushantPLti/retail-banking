package com.transaction.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.dto.AccStatementDto;
import com.transaction.dto.FundTransferDto;
import com.transaction.dto.TransactionResponse;
import com.transaction.exception.AccountNotFoundException;
import com.transaction.exception.NoTransactionsFoundException;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionDto;
import com.transaction.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RefreshScope
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Performs a new transaction.
     *
     * @param accountNumber, amount, TransactionType type (credit/withdrawn)
     * @return transaction details
     */
    @Operation(summary = "Perform a transaction", description = "Executes a transaction for the provided account number, amount, and transaction type.")
    @PostMapping
    public ResponseEntity<TransactionResponse<Transaction>> performTransaction(
            @Valid @RequestBody TransactionDto transaction) {
        try {
            TransactionResponse<Transaction> trans = transactionService.performTransaction(transaction);

            return ResponseEntity.status(trans.getStatus()).body(trans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionResponse<>(null, e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * Fetches all the transaction for a particular account
     *
     * @param accountNumber
     * @return List of all transaction for a particular account
     */
    @Operation(summary = "Get account transactions", description = "Retrieves all transactions for a specific account number.")
    @PostMapping("/statement")
    public ResponseEntity<List<Transaction>> getTransaction(@Valid @RequestBody AccStatementDto accountStatement,
                                                            @PageableDefault(size = 10) Pageable pageable) {
        List<Transaction> transactions = transactionService.getAllTransactions(accountStatement.getAccountNumber(), pageable);
        Optional.ofNullable(transactions).filter(list -> !list.isEmpty())
                .orElseThrow(() -> new AccountNotFoundException(
                        "No transactions found for account number: " + accountStatement.getAccountNumber()));

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Performs a fund transfer.
     *
     * @param senderAccount, amount, receiverAccount
     * @return void
     */
    @Operation(summary = "Transfer funds", description = "Transfers funds from the sender's account to the receiver's account.")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse<Transaction>> transferFunds(
            @Valid @RequestBody FundTransferDto fundTransfer) {
        try {
            Transaction transferFunds = transactionService.transferFunds(fundTransfer.getSenderAccount(),
                    fundTransfer.getReceiverAccount(), fundTransfer.getAmount());
            return ResponseEntity.ok(new TransactionResponse<>(transferFunds,
                    "Funds Transferred successful from " + fundTransfer.getSenderAccount() + " to "
                            + fundTransfer.getReceiverAccount() + " of " + fundTransfer.getAmount() + " amount",
                    HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionResponse<>(null, e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @Operation(summary = "Get transactions for a period", description = "Retrieves transactions for a specific account number within a defined date range.")
    @GetMapping("/transactionPeriod")
    public ResponseEntity<List<Transaction>> getTransaction(@RequestParam Long accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<Transaction> transactions = transactionService.getPeriodTransaction(accountNumber, fromDate, toDate);
        if (transactions.isEmpty())
            return ResponseEntity.noContent().build();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Fetches transactions for the last quarter.
     *
     * @param accountNumber
     * @return List of transactions for the last quarter
     */
    @Operation(summary = "Get quarterly transactions", description = "Retrieves transactions for a specific account number for the last quarter.")
    @GetMapping("/quarterlyTransactions")
    public ResponseEntity<List<Transaction>> getQuarterlyTransaction(@RequestParam Long accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getQuarterlyTransaction(accountNumber);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (AccountNotFoundException | NoTransactionsFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Fetches transactions for the last n days.
     *
     * @param accountNumber
     * @param days
     * @return List of transactions for the last n days
     */
    @Operation(summary = "Get transactions for the last n days", description = "Retrieves transactions for a specific account number for the last n days.")
    @GetMapping("/lastTransactions")
    public ResponseEntity<List<Transaction>> getLastTransaction(@RequestParam Long accountNumber, @RequestParam int days) {
        try {
            List<Transaction> transactions = transactionService.getLastTransaction(accountNumber, days);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (AccountNotFoundException | NoTransactionsFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
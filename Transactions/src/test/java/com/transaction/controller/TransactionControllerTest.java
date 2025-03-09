package com.transaction.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.transaction.dto.AccStatementDto;
import com.transaction.dto.FundTransferDto;
import com.transaction.dto.TransactionResponse;
import com.transaction.exception.AccountNotFoundException;
import com.transaction.exception.NoTransactionsFoundException;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionDto;
import com.transaction.model.TransactionType;
import com.transaction.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private FundTransferDto fundTransferDto;
    private AccStatementDto accStatementDto;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(123456L);
        transactionDto.setAmount(500.0);
        transactionDto.setTransactionType(TransactionType.DEBIT);

        fundTransferDto = new FundTransferDto();
        fundTransferDto.setSenderAccount(123456L);
        fundTransferDto.setReceiverAccount(654321L);
        fundTransferDto.setAmount(500.0);

        accStatementDto = new AccStatementDto();
        accStatementDto.setAccountNumber(123456L);

        transaction = new Transaction();
        transaction.setAccountNumber(123456L);
        transaction.setTransactionAmount(500.0);
        transaction.setTransactionType(TransactionType.DEBIT);
    }

    @Test
    void testPerformTransaction() throws Exception {
        TransactionResponse<Transaction> transactionResponse = new TransactionResponse<>(transaction, "Success", HttpStatus.OK);
        when(transactionService.performTransaction(any(TransactionDto.class))).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse<Transaction>> response = transactionController.performTransaction(transactionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody().getData());
    }

    @Test
    void testPerformTransaction_Exception() {
        try {
			when(transactionService.performTransaction(any(TransactionDto.class))).thenThrow(new RuntimeException("Error"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        ResponseEntity<TransactionResponse<Transaction>> response = transactionController.performTransaction(transactionDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
    }

    @Test
    void testGetTransaction() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getTransaction(123456L)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getTransaction(accStatementDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetTransaction_NoTransactionsFound() {
        when(transactionService.getTransaction(123456L)).thenReturn(Collections.emptyList());

        assertThrows(AccountNotFoundException.class, () -> {
            transactionController.getTransaction(accStatementDto);
        });
    }

    @Test
    void testTransferFunds() throws Exception {
        TransactionResponse<Transaction> transactionResponse = new TransactionResponse<>(transaction, "Funds Transferred", HttpStatus.OK);
        when(transactionService.transferFunds(any(Long.class), any(Long.class), any(Double.class))).thenReturn(transaction);

        ResponseEntity<TransactionResponse<Transaction>> response = transactionController.transferFunds(fundTransferDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody().getData());
    }

    @Test
    void testTransferFunds_Exception() {
        try {
			when(transactionService.transferFunds(any(Long.class), any(Long.class), any(Double.class))).thenThrow(new RuntimeException("Error"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        ResponseEntity<TransactionResponse<Transaction>> response = transactionController.transferFunds(fundTransferDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
    }

    @Test
    void testGetTransactionPeriod() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getPeriodTransaction(123456L, LocalDate.now().minusDays(10), LocalDate.now())).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getTransaction(123456L, LocalDate.now().minusDays(10), LocalDate.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetQuarterlyTransaction() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getQuarterlyTransaction(123456L)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getQuarterlyTransaction(123456L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetQuarterlyTransaction_Exception() {
        when(transactionService.getQuarterlyTransaction(123456L)).thenThrow(new AccountNotFoundException("No transactions found"));

        ResponseEntity<List<Transaction>> response = transactionController.getQuarterlyTransaction(123456L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetLastTransaction() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getLastTransaction(123456L, 10)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getLastTransaction(123456L, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetLastTransaction_Exception() {
        when(transactionService.getLastTransaction(123456L, 10)).thenThrow(new AccountNotFoundException("No transactions found"));

        ResponseEntity<List<Transaction>> response = transactionController.getLastTransaction(123456L, 10);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
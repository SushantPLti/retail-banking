package com.transaction.periodTransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transaction.exception.AccountNotFoundException;
import com.transaction.exception.NoTransactionsFoundException;
import com.transaction.model.Transaction;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.TransactioServiceImpl;

@ExtendWith(MockitoExtension.class)
class PeriodTransactionTest {

    @InjectMocks
    private TransactioServiceImpl service;

    @Mock
    private TransactionRepository transactionRepository;

    private List<Transaction> transactions;
    private Long accountNumber;
    private LocalDate fromDate;
    private LocalDate toDate;

    @BeforeEach
    void setUp() {
        accountNumber = 123456L;
        fromDate = LocalDate.of(2023, 1, 1);
        toDate = LocalDate.of(2023, 12, 31);

        transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setAccountNumber(accountNumber);
        transaction1.setCreatedAt(LocalDateTime.of(2023, 5, 15, 10, 0));
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountNumber(accountNumber);
        transaction2.setCreatedAt(LocalDateTime.of(2023, 7, 20, 15, 30));
        transactions.add(transaction2);
    }

    @Test
    void testGetPeriodTransaction_Success() {
        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(accountNumber)).thenReturn(transactions);

        List<Transaction> result = service.getPeriodTransaction(accountNumber, fromDate, toDate);

        assertEquals(2, result.size());
        assertEquals(accountNumber, result.get(0).getAccountNumber());
        assertEquals(accountNumber, result.get(1).getAccountNumber());
    }

    @Test
    void testGetPeriodTransaction_NoTransactionsFound() {
        // Ensure the transactions list contains transactions outside the specified date range
        transactions.clear();
        Transaction transaction1 = new Transaction();
        transaction1.setAccountNumber(accountNumber);
        transaction1.setCreatedAt(LocalDateTime.of(2022, 12, 31, 23, 59)); // Before fromDate
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountNumber(accountNumber);
        transaction2.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0)); // After toDate
        transactions.add(transaction2);

        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(accountNumber)).thenReturn(transactions);

        NoTransactionsFoundException exception = assertThrows(NoTransactionsFoundException.class, () -> {
            service.getPeriodTransaction(accountNumber, fromDate, toDate);
        });

        assertEquals("No transactions found for the specified date range.", exception.getMessage());
    }

    @Test
    void testGetPeriodTransaction_AccountNotFound() {
        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(accountNumber)).thenReturn(null);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            service.getPeriodTransaction(accountNumber, fromDate, toDate);
        });

        assertEquals("No transactions found for account number: " + accountNumber, exception.getMessage());
    }
}
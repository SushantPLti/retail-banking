package com.transaction.periodTransaction;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.transaction.model.TransactionType;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.TransactioServiceImpl;
import com.transaction.util.CustomDateTime;

@ExtendWith(MockitoExtension.class)
class NthTransactionTest {

    @InjectMocks
    private TransactioServiceImpl service;

    @Mock
    private TransactionRepository transactionRepository;

    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setAccountNumber(123456L);
        transaction1.setTransactionAmount(500.0);
        transaction1.setTransactionType(TransactionType.DEBIT);
        transaction1.setCreatedAt(LocalDateTime.now().minusDays(5));
        transaction1.setClosingBalance(1000.0);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountNumber(123456L);
        transaction2.setTransactionAmount(200.0);
        transaction2.setTransactionType(TransactionType.CREDIT);
        transaction2.setCreatedAt(LocalDateTime.now().minusDays(10));
        transaction2.setClosingBalance(1200.0);

        transactions.add(transaction1);
        transactions.add(transaction2);
    }

    @Test
    void testGetLastTransaction() {
        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(anyLong())).thenReturn(transactions);

        List<Transaction> response = service.getLastTransaction(123456L, 7);

        assertEquals(1, response.size());
        assertEquals(123456L, response.get(0).getAccountNumber());
        assertEquals(500.0, response.get(0).getTransactionAmount());
    }

    @Test
    void testGetLastTransaction_NoTransactionsFound() {
        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(anyLong())).thenReturn(new ArrayList<>());

        NoTransactionsFoundException exception = assertThrows(NoTransactionsFoundException.class, () -> {
            service.getLastTransaction(123456L, 7);
        });

        assertEquals("No transaction exists in the last 7 specified days", exception.getMessage());
    }

    @Test
    void testGetQuarterlyTransaction() {
        // Adjust the transaction dates to fall within the last quarter
        LocalDate now = LocalDate.now();
        LocalDate startOfQuarter;
        if (now.getMonthValue() <= 3) {
            startOfQuarter = LocalDate.of(now.getYear() - 1, 10, 1); // Last year's Q4
        } else if (now.getMonthValue() <= 6) {
            startOfQuarter = LocalDate.of(now.getYear(), 1, 1); // Q1
        } else if (now.getMonthValue() <= 9) {
            startOfQuarter = LocalDate.of(now.getYear(), 4, 1); // Q2
        } else {
            startOfQuarter = LocalDate.of(now.getYear(), 7, 1); // Q3
        }

        transactions.get(0).setCreatedAt(LocalDateTime.of(startOfQuarter.plusDays(1), LocalTime.MIDNIGHT));
        transactions.get(1).setCreatedAt(LocalDateTime.of(startOfQuarter.plusDays(2), LocalTime.MIDNIGHT));

        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(anyLong())).thenReturn(transactions);

        List<Transaction> response = service.getQuarterlyTransaction(123456L);

        assertEquals(2, response.size());
        assertEquals(123456L, response.get(0).getAccountNumber());
        assertEquals(500.0, response.get(0).getTransactionAmount());
    }

    @Test
    void testGetQuarterlyTransaction_NoTransactionsFound() {
        when(transactionRepository.findByAccountNumberOrderByTransactionIdDesc(anyLong())).thenReturn(new ArrayList<>());

        NoTransactionsFoundException exception = assertThrows(NoTransactionsFoundException.class, () -> {
            service.getQuarterlyTransaction(123456L);
        });

        assertEquals("No transaction exists in the last quarter", exception.getMessage());
    }
}
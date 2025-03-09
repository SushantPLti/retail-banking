package com.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.transaction.dto.AccountCurrencyDTO;
import com.transaction.dto.TransactionResponse;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionDto;
import com.transaction.model.TransactionType;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.AccountClient;
import com.transaction.service.TransactionService;
import com.transaction.service.TransactioServiceImpl;
import com.transaction.util.CustomDateTime;

@ExtendWith(MockitoExtension.class)
class TransactionsApplicationTests {

    @InjectMocks
    private TransactioServiceImpl service;

    @Mock
    private AccountClient accountClient;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionDto transactionDto;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(123456L);
        transactionDto.setAmount(500.0);
        transactionDto.setTransactionType(TransactionType.CREDIT);

        transaction = new Transaction();
        transaction.setAccountNumber(123456L);
        transaction.setTransactionAmount(500.0);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setCreatedAt(CustomDateTime.getTimeSpecificZone());
        transaction.setClosingBalance(1000.0);
    }

    @Test
    void testPerformTransaction() throws Exception {
        AccountCurrencyDTO accountCurrencyDTO = new AccountCurrencyDTO();
        accountCurrencyDTO.setBalance(500.0);
        accountCurrencyDTO.setMinBalance(100.0);

        when(accountClient.getBalance(any(Long.class))).thenReturn(accountCurrencyDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse<Transaction> response = service.performTransaction(transactionDto);

        assert response.getStatus() == HttpStatus.CREATED;
        assert response.getData().getAccountNumber().equals(transactionDto.getAccountNumber());
        assert response.getData().getTransactionAmount().equals(transactionDto.getAmount());
        assert response.getData().getTransactionType().equals(transactionDto.getTransactionType());
    }
    
    
}
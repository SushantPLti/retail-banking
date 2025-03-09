package com.transaction.fundTransfer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transaction.dto.AccountCurrencyDTO;
import com.transaction.dto.FundTransferDto;
import com.transaction.exception.InsufficientBalanceException;
import com.transaction.exception.MinBalanceLimitCrossException;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionType;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.AccountClient;
import com.transaction.service.TransactioServiceImpl;
import com.transaction.util.CustomDateTime;

@ExtendWith(MockitoExtension.class)
class TransactionFundTransferTest {

    @InjectMocks
    private TransactioServiceImpl service;

    @Mock
    private AccountClient accountClient;

    @Mock
    private TransactionRepository transactionRepository;

    private FundTransferDto fundTransferDto;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        fundTransferDto = new FundTransferDto();
        fundTransferDto.setSenderAccount(123456L);
        fundTransferDto.setReceiverAccount(654321L);
        fundTransferDto.setAmount(500.0);

        transaction = new Transaction();
        transaction.setAccountNumber(123456L);
        transaction.setTransactionAmount(500.0);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setCreatedAt(CustomDateTime.getTimeSpecificZone());
        transaction.setClosingBalance(1000.0);
    }

    @Test
    void testTransferFunds() throws Exception {
        AccountCurrencyDTO senderAccountDTO = new AccountCurrencyDTO();
        senderAccountDTO.setBalance(1000.0);
        senderAccountDTO.setMinBalance(100.0);
        senderAccountDTO.setCurrency("USD");

        AccountCurrencyDTO receiverAccountDTO = new AccountCurrencyDTO();
        receiverAccountDTO.setBalance(500.0);
        receiverAccountDTO.setCurrency("USD");

        when(accountClient.getBalance(fundTransferDto.getSenderAccount())).thenReturn(senderAccountDTO);
        when(accountClient.getBalance(fundTransferDto.getReceiverAccount())).thenReturn(receiverAccountDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction response = service.transferFunds(
                fundTransferDto.getSenderAccount(),
                fundTransferDto.getReceiverAccount(),
                fundTransferDto.getAmount()
        );

        assertEquals(fundTransferDto.getSenderAccount(), response.getAccountNumber());
        assertEquals(fundTransferDto.getAmount(), response.getTransactionAmount());
        assertEquals(TransactionType.DEBIT, response.getTransactionType());
    }

    @Test
    void testTransferFunds_InsufficientBalance() {
        AccountCurrencyDTO senderAccountDTO = new AccountCurrencyDTO();
        senderAccountDTO.setBalance(400.0);
        senderAccountDTO.setMinBalance(100.0);

        when(accountClient.getBalance(fundTransferDto.getSenderAccount())).thenReturn(senderAccountDTO);

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            service.transferFunds(
                    fundTransferDto.getSenderAccount(),
                    fundTransferDto.getReceiverAccount(),
                    fundTransferDto.getAmount()
            );
        });

        assertEquals("Insufficient balance in sender's account", exception.getMessage());
    }

//    @Test
//    void testTransferFunds_MinBalanceLimitCross() {
//        AccountCurrencyDTO senderAccountDTO = new AccountCurrencyDTO();
//        senderAccountDTO.setBalance(600.0);
//        senderAccountDTO.setMinBalance(200.0);
//        senderAccountDTO.setCurrency("USD"); // Ensure sender currency is not null
//
//        AccountCurrencyDTO receiverAccountDTO = new AccountCurrencyDTO();
//        receiverAccountDTO.setBalance(500.0); // Ensure receiver balance is not null
//        receiverAccountDTO.setCurrency("USD"); // Ensure receiver currency is not null
//
//        when(accountClient.getBalance(fundTransferDto.getSenderAccount())).thenReturn(senderAccountDTO);
//        when(accountClient.getBalance(fundTransferDto.getReceiverAccount())).thenReturn(receiverAccountDTO);
//
//        MinBalanceLimitCrossException exception = assertThrows(MinBalanceLimitCrossException.class, () -> {
//            service.transferFunds(
//                    fundTransferDto.getSenderAccount(),
//                    fundTransferDto.getReceiverAccount(),
//                    fundTransferDto.getAmount()
//            );
//        });
//
//        assertEquals("The transaction could not be completed because the sender's account balance is below the minimum required limit.", exception.getMessage());
//    }
}
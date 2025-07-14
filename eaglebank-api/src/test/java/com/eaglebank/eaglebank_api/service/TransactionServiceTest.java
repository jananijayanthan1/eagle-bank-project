package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;
import com.eaglebank.eaglebank_api.exception.NotFoundException;
import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.Transaction;
import com.eaglebank.eaglebank_api.model.request.CreateTransactionRequest;
import com.eaglebank.eaglebank_api.repository.BankAccountRepository;
import com.eaglebank.eaglebank_api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @InjectMocks
    private TransactionService transactionService;

    private final String userId = "usr-abc123";
    private final String accountNumber = "01234567";

    private BankAccount account;

    @BeforeEach
    void setup() {
        account = new BankAccount();
        account.setAccountNumber(accountNumber);
        account.setUserId(userId);
        account.setBalance(500.0);
        account.setCurrency(Currency.GBP);
        account.setAccountType(com.eaglebank.eaglebank_api.enums.AccountType.personal);
        account.setName("Test Account");
        account.setSortCode("00-00-00");
        account.setCreatedTimestamp(OffsetDateTime.now());
        account.setUpdatedTimestamp(OffsetDateTime.now());
    }

    @Test
    void createTransaction_deposit_success() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.deposit);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        Transaction tx = transactionService.createTransaction(accountNumber, userId, req);
        assertEquals(accountNumber, tx.getAccountNumber());
        assertEquals(userId, tx.getUserId());
        assertEquals(600.0, account.getBalance());
        assertEquals(TransactionType.deposit, tx.getType());
        assertEquals(Currency.GBP, tx.getCurrency());
        assertTrue(tx.getId().matches("^tan-[A-Za-z0-9]+$"));
    }

    @Test
    void createTransaction_withdrawal_success() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(200.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.withdrawal);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        Transaction tx = transactionService.createTransaction(accountNumber, userId, req);
        assertEquals(300.0, account.getBalance());
        assertEquals(TransactionType.withdrawal, tx.getType());
    }

    @Test
    void createTransaction_insufficientFunds() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(1000.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.withdrawal);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        assertThrows(IllegalArgumentException.class, () ->
            transactionService.createTransaction(accountNumber, userId, req)
        );
    }

    @Test
    void createTransaction_forbiddenUser() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.deposit);
        account.setUserId("other-user");
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        assertThrows(RuntimeException.class, () ->
            transactionService.createTransaction(accountNumber, userId, req)
        );
    }

    @Test
    void createTransaction_accountNotFound() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency(java.util.Currency.getInstance("GBP"));
        req.setType(TransactionType.deposit);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThrows(NotFoundException.class, () ->
            transactionService.createTransaction(accountNumber, userId, req)
        );
    }

    @Test
    void createTransaction_currencyNotGBP() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency(java.util.Currency.getInstance("USD"));
        req.setType(TransactionType.deposit);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        assertThrows(RuntimeException.class, () ->
            transactionService.createTransaction(accountNumber, userId, req)
        );
    }

    @Test
    void listTransactions_success() {
        Transaction tx = new Transaction();
        tx.setId("tan-abc123");
        tx.setAccountNumber(accountNumber);
        tx.setUserId(userId);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(transactionRepository.findByAccountNumber(accountNumber)).thenReturn(Collections.singletonList(tx));
        var result = transactionService.listTransactions(accountNumber, userId);
        assertEquals(1, result.size());
        assertEquals("tan-abc123", result.get(0).getId());
    }

    @Test
    void listTransactions_forbiddenUser() {
        account.setUserId("other-user");
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        assertThrows(RuntimeException.class, () ->
            transactionService.listTransactions(accountNumber, userId)
        );
    }

    @Test
    void listTransactions_accountNotFound() {
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThrows(NotFoundException.class, () ->
            transactionService.listTransactions(accountNumber, userId)
        );
    }

    @Test
    void fetchTransactionById_success() {
        Transaction tx = new Transaction();
        tx.setId("tan-abc123");
        tx.setAccountNumber(accountNumber);
        tx.setUserId(userId);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(transactionRepository.findByAccountNumberAndId(accountNumber, "tan-abc123"))
            .thenReturn(Optional.of(tx));
        var result = transactionService.fetchTransactionById(accountNumber, "tan-abc123", userId);
        assertEquals("tan-abc123", result.getId());
    }

    @Test
    void fetchTransactionById_forbiddenUser() {
        account.setUserId("other-user");
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        assertThrows(RuntimeException.class, () ->
            transactionService.fetchTransactionById(accountNumber, "tan-abc123", userId)
        );
    }

    @Test
    void fetchTransactionById_accountNotFound() {
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThrows(NotFoundException.class, () ->
            transactionService.fetchTransactionById(accountNumber, "tan-abc123", userId)
        );
    }

    @Test
    void fetchTransactionById_transactionNotFound() {
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(transactionRepository.findByAccountNumberAndId(accountNumber, "tan-abc123"))
            .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () ->
            transactionService.fetchTransactionById(accountNumber, "tan-abc123", userId)
        );
    }
} 
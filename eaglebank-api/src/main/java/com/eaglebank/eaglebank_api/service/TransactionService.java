package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.enums.TransactionType;
import com.eaglebank.eaglebank_api.exception.NotFoundException;
import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.Transaction;
import com.eaglebank.eaglebank_api.model.request.CreateTransactionRequest;
import com.eaglebank.eaglebank_api.repository.BankAccountRepository;
import com.eaglebank.eaglebank_api.repository.TransactionRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private static final int MAX_RETRIES = 3;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public Transaction createTransaction(String accountNumber, String userId, CreateTransactionRequest request) {
        int retryCount = 0;
        
        while (retryCount < MAX_RETRIES) {
            try {
                return performTransaction(accountNumber, userId, request);
            } catch (OptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= MAX_RETRIES) {
                    throw new RuntimeException("Transaction failed after " + MAX_RETRIES + " retries due to concurrent modification", e);
                }
                // Small delay before retry to reduce contention
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Transaction interrupted", ie);
                }
            }
        }
        
        throw new RuntimeException("Unexpected error in transaction processing");
    }

    private Transaction performTransaction(String accountNumber, String userId, CreateTransactionRequest request) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden: user does not own this account");
        }
        if (!"GBP".equals(request.getCurrency().getCurrencyCode())) {
            throw new RuntimeException("Only GBP currency is supported");
        }
        if (request.getType() == TransactionType.withdrawal && account.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        // Update account balance
        if (request.getType() == TransactionType.deposit) {
            account.setBalance(account.getBalance() + request.getAmount());
        } else {
            account.setBalance(account.getBalance() - request.getAmount());
        }
        account.setUpdatedTimestamp(OffsetDateTime.now());
        bankAccountRepository.save(account);
        
        Transaction transaction = new Transaction();
        // Generate a valid transaction ID matching ^tan-[A-Za-z0-9]+$
        String random = Long.toString(System.currentTimeMillis(), 36) + Integer.toString(new Random().nextInt(1000), 36);
        transaction.setId("tan-" + random);
        transaction.setAccountNumber(accountNumber);
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(com.eaglebank.eaglebank_api.enums.Currency.valueOf(request.getCurrency().getCurrencyCode()));
        transaction.setType(request.getType());
        transaction.setCreatedTimestamp(OffsetDateTime.now());
        
        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            transactionRepository.flush();
            return savedTransaction;
        } catch (Exception e) {
            System.err.println("Failed to save transaction: " + e.getMessage());
            System.out.println("error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Transaction> listTransactions(String accountNumber, String userId) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden: user does not own this account");
        }
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public Transaction fetchTransactionById(String accountNumber, String transactionId, String userId) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden: user does not own this account");
        }
        return transactionRepository.findByAccountNumberAndId(accountNumber, transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }
} 
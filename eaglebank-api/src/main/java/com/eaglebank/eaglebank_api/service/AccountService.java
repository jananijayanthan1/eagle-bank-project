package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.request.CreateBankAccountRequest;
import com.eaglebank.eaglebank_api.model.response.BankAccountResponse;
import com.eaglebank.eaglebank_api.repository.BankAccountRepository;
import com.eaglebank.eaglebank_api.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.model.response.ListBankAccountsResponse;
import java.util.List;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public AccountService(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Transactional
    public BankAccountResponse createAccount(String userId, String sortCode, CreateBankAccountRequest request) {
        try {
            String accountNumber = generateAccountNumber();
            
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId("acc-" + System.currentTimeMillis() + "-" + new Random().nextInt(1000));
            bankAccount.setAccountNumber(accountNumber);
            bankAccount.setSortCode("10-10-10"); // Use the fixed sort code from init.sql
            bankAccount.setName(request.getName());
            bankAccount.setAccountType(AccountType.personal); // Use personal to match init.sql constraint
            bankAccount.setBalance(0.0);
            bankAccount.setCurrency(Currency.GBP); // Use GBP to match init.sql constraint
            bankAccount.setUserId(userId);
            bankAccount.setCreatedTimestamp(OffsetDateTime.now());
            bankAccount.setUpdatedTimestamp(OffsetDateTime.now());
            
            bankAccount = bankAccountRepository.save(bankAccount);
            
            BankAccountResponse response = bankAccountMapper.toResponse(bankAccount);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    public BankAccountResponse fetchAccountByAccountNumber(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Bank account not found");
        }
        return bankAccountMapper.toResponse(account);
    }

    public ListBankAccountsResponse listAccounts(String userId) {
        List<BankAccount> accounts = bankAccountRepository.findByUserId(userId);
        ListBankAccountsResponse response = new ListBankAccountsResponse();
        response.setAccounts(bankAccountMapper.toResponseList(accounts));
        return response;
    }

    public BankAccount getBankAccountEntityByAccountNumber(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Bank account not found");
        }
        return account;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        int number = random.nextInt(1000000); // 6 digits
        return String.format("01%06d", number);
    }
} 
package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.mapper.BankAccountMapper;
import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.request.CreateBankAccountRequest;
import com.eaglebank.eaglebank_api.model.response.BankAccountResponse;
import com.eaglebank.eaglebank_api.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountMapper bankAccountMapper;
    @InjectMocks
    private AccountService accountService;

    private CreateBankAccountRequest validRequest;
    private BankAccount savedAccount;
    private BankAccountResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validRequest = new CreateBankAccountRequest();
        validRequest.setName("Personal Bank Account");
        validRequest.setAccountType(AccountType.personal);

        savedAccount = new BankAccount();
        savedAccount.setId("acc-123");
        savedAccount.setAccountNumber("01234567");
        savedAccount.setSortCode("10-10-10");
        savedAccount.setName("Personal Bank Account");
        savedAccount.setAccountType(AccountType.personal);
        savedAccount.setBalance(0.0);
        savedAccount.setCurrency(Currency.GBP);
        savedAccount.setUserId("usr-123");
        savedAccount.setCreatedTimestamp(OffsetDateTime.now());
        savedAccount.setUpdatedTimestamp(OffsetDateTime.now());

        response = new BankAccountResponse();
        response.setAccountNumber("01234567");
        response.setSortCode("10-10-10");
        response.setName("Personal Bank Account");
        response.setAccountType(AccountType.personal);
        response.setBalance(0.0);
        response.setCurrency(Currency.GBP);
        response.setCreatedTimestamp(savedAccount.getCreatedTimestamp());
        response.setUpdatedTimestamp(savedAccount.getUpdatedTimestamp());
    }

    @Test
    void createAccount_ValidRequest_Success() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);
        when(bankAccountMapper.toResponse(any(BankAccount.class))).thenReturn(response);

        BankAccountResponse result = accountService.createAccount("usr-123", "10-10-10", validRequest);

        assertNotNull(result);
        assertEquals("01234567", result.getAccountNumber());
        assertEquals("10-10-10", result.getSortCode());
        assertEquals("Personal Bank Account", result.getName());
        assertEquals(AccountType.personal, result.getAccountType());
        assertEquals(0.0, result.getBalance());
        assertEquals(Currency.GBP, result.getCurrency());
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
        verify(bankAccountMapper, times(1)).toResponse(any(BankAccount.class));
    }

    @Test
    void createAccount_RepositoryThrowsException_Propagates() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () ->
                accountService.createAccount("usr-123", "10-10-10", validRequest));
    }

    @Test
    void createAccount_AccountNumberFormat() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);
        when(bankAccountMapper.toResponse(any(BankAccount.class))).thenReturn(response);
        ArgumentCaptor<BankAccount> captor = ArgumentCaptor.forClass(BankAccount.class);

        accountService.createAccount("usr-123", "10-10-10", validRequest);
        verify(bankAccountRepository).save(captor.capture());
        String accountNumber = captor.getValue().getAccountNumber();
        assertTrue(accountNumber.matches("^01\\d{6}$"), "Account number should match ^01\\d{6}$");
    }

    @Test
    void createAccount_SetsTimestamps() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);
        when(bankAccountMapper.toResponse(any(BankAccount.class))).thenReturn(response);
        ArgumentCaptor<BankAccount> captor = ArgumentCaptor.forClass(BankAccount.class);

        accountService.createAccount("usr-123", "10-10-10", validRequest);
        verify(bankAccountRepository).save(captor.capture());
        assertNotNull(captor.getValue().getCreatedTimestamp());
        assertNotNull(captor.getValue().getUpdatedTimestamp());
    }

    @Test
    void createAccount_SetsUserIdAndDefaults() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);
        when(bankAccountMapper.toResponse(any(BankAccount.class))).thenReturn(response);
        ArgumentCaptor<BankAccount> captor = ArgumentCaptor.forClass(BankAccount.class);

        accountService.createAccount("usr-999", "10-10-10", validRequest);
        verify(bankAccountRepository).save(captor.capture());
        assertEquals("usr-999", captor.getValue().getUserId());
        assertEquals(0.0, captor.getValue().getBalance());
        assertEquals(Currency.GBP, captor.getValue().getCurrency());
        assertEquals(AccountType.personal, captor.getValue().getAccountType());
    }
} 
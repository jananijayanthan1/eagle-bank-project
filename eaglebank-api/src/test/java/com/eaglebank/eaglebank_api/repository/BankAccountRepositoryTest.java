package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.model.BankAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    @DisplayName("findByUserId returns all accounts for a user")
    void findByUserId_ReturnsAccounts() {
        BankAccount acc1 = new BankAccount();
        acc1.setId("acc-1");
        acc1.setAccountNumber("01000001");
        acc1.setSortCode("10-10-10");
        acc1.setName("Account 1");
        acc1.setAccountType(AccountType.personal);
        acc1.setBalance(100.0);
        acc1.setCurrency(Currency.GBP);
        acc1.setUserId("usr-1");
        acc1.setCreatedTimestamp(OffsetDateTime.now());
        acc1.setUpdatedTimestamp(OffsetDateTime.now());

        BankAccount acc2 = new BankAccount();
        acc2.setId("acc-2");
        acc2.setAccountNumber("01000002");
        acc2.setSortCode("10-10-10");
        acc2.setName("Account 2");
        acc2.setAccountType(AccountType.personal);
        acc2.setBalance(200.0);
        acc2.setCurrency(Currency.GBP);
        acc2.setUserId("usr-1");
        acc2.setCreatedTimestamp(OffsetDateTime.now());
        acc2.setUpdatedTimestamp(OffsetDateTime.now());

        BankAccount acc3 = new BankAccount();
        acc3.setId("acc-3");
        acc3.setAccountNumber("01000003");
        acc3.setSortCode("10-10-10");
        acc3.setName("Account 3");
        acc3.setAccountType(AccountType.personal);
        acc3.setBalance(300.0);
        acc3.setCurrency(Currency.GBP);
        acc3.setUserId("usr-2");
        acc3.setCreatedTimestamp(OffsetDateTime.now());
        acc3.setUpdatedTimestamp(OffsetDateTime.now());

        bankAccountRepository.save(acc1);
        bankAccountRepository.save(acc2);
        bankAccountRepository.save(acc3);

        List<BankAccount> user1Accounts = bankAccountRepository.findByUserId("usr-1");
        assertEquals(2, user1Accounts.size());
        assertTrue(user1Accounts.stream().anyMatch(a -> a.getAccountNumber().equals("01000001")));
        assertTrue(user1Accounts.stream().anyMatch(a -> a.getAccountNumber().equals("01000002")));

        List<BankAccount> user2Accounts = bankAccountRepository.findByUserId("usr-2");
        assertEquals(1, user2Accounts.size());
        assertEquals("01000003", user2Accounts.get(0).getAccountNumber());
    }

    @Test
    @DisplayName("findByAccountNumber returns the correct account")
    void findByAccountNumber_ReturnsAccount() {
        BankAccount acc = new BankAccount();
        acc.setId("acc-4");
        acc.setAccountNumber("01000004");
        acc.setSortCode("10-10-10");
        acc.setName("Account 4");
        acc.setAccountType(AccountType.personal);
        acc.setBalance(400.0);
        acc.setCurrency(Currency.GBP);
        acc.setUserId("usr-3");
        acc.setCreatedTimestamp(OffsetDateTime.now());
        acc.setUpdatedTimestamp(OffsetDateTime.now());

        bankAccountRepository.save(acc);

        BankAccount found = bankAccountRepository.findByAccountNumber("01000004");
        assertNotNull(found);
        assertEquals("usr-3", found.getUserId());
        assertEquals(400.0, found.getBalance());
    }

    @Test
    @DisplayName("findByAccountNumber returns null for non-existent account")
    void findByAccountNumber_NonExistent_ReturnsNull() {
        BankAccount found = bankAccountRepository.findByAccountNumber("01999999");
        assertNull(found);
    }
} 
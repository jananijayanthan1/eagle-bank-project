package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;
import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccount account;

    @BeforeEach
    void setup() {
        account = new BankAccount();
        account.setId("acc-123456"); // Manually assign ID
        account.setAccountNumber("01234567");
        account.setUserId("usr-abc123");
        account.setBalance(1000.0);
        account.setCurrency(Currency.GBP);
        account.setAccountType(com.eaglebank.eaglebank_api.enums.AccountType.personal);
        account.setName("Test Account");
        account.setSortCode("00-00-00");
        account.setCreatedTimestamp(OffsetDateTime.now());
        account.setUpdatedTimestamp(OffsetDateTime.now());
        bankAccountRepository.save(account);
    }

    @Test
    void saveAndFindByAccountNumber() {
        Transaction tx1 = new Transaction();
        tx1.setId("tan-abc123");
        tx1.setAccountNumber(account.getAccountNumber());
        tx1.setUserId(account.getUserId());
        tx1.setAmount(100.0);
        tx1.setCurrency(Currency.GBP);
        tx1.setType(TransactionType.deposit);
        tx1.setCreatedTimestamp(OffsetDateTime.now());
        transactionRepository.save(tx1);

        Transaction tx2 = new Transaction();
        tx2.setId("tan-def456");
        tx2.setAccountNumber(account.getAccountNumber());
        tx2.setUserId(account.getUserId());
        tx2.setAmount(50.0);
        tx2.setCurrency(Currency.GBP);
        tx2.setType(TransactionType.withdrawal);
        tx2.setCreatedTimestamp(OffsetDateTime.now());
        transactionRepository.save(tx2);

        List<Transaction> found = transactionRepository.findByAccountNumber(account.getAccountNumber());
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(t -> t.getId().equals("tan-abc123")));
        assertTrue(found.stream().anyMatch(t -> t.getId().equals("tan-def456")));
    }

    @Test
    void findByAccountNumberAndId_found() {
        Transaction tx = new Transaction();
        tx.setId("tan-xyz789");
        tx.setAccountNumber(account.getAccountNumber());
        tx.setUserId(account.getUserId());
        tx.setAmount(200.0);
        tx.setCurrency(Currency.GBP);
        tx.setType(TransactionType.deposit);
        tx.setCreatedTimestamp(OffsetDateTime.now());
        transactionRepository.save(tx);

        Optional<Transaction> found = transactionRepository.findByAccountNumberAndId(account.getAccountNumber(), "tan-xyz789");
        assertTrue(found.isPresent());
        assertEquals("tan-xyz789", found.get().getId());
    }

    @Test
    void findByAccountNumberAndId_notFound() {
        Optional<Transaction> found = transactionRepository.findByAccountNumberAndId(account.getAccountNumber(), "tan-nonexistent");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByAccountNumber_empty() {
        List<Transaction> found = transactionRepository.findByAccountNumber("nonexistent");
        assertTrue(found.isEmpty());
    }
} 
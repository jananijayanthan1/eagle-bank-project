package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByUserId(String userId);
    BankAccount findByAccountNumber(String accountNumber);
} 
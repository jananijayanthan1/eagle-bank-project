package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumber(String accountNumber);
    Optional<Transaction> findByAccountNumberAndId(String accountNumber, String id);
} 
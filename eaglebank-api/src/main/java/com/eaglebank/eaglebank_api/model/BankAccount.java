package com.eaglebank.eaglebank_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.eaglebank.eaglebank_api.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    //needs to be a custom format like 01XXXXXX
    private String accountNumber;

    private String accountType;

    private String accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String name;

    //relation to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance;

    private Currency currency;
    
    private String sortCode;
}

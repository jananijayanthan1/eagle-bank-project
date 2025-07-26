package com.eaglebank.eaglebank_api.model;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bank_accounts")
@Data
public class BankAccount {

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = "^01\\d{6}$")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @Column(name = "sort_code")
    private String sortCode;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "10000.00", inclusive = true)
    private Double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "created_timestamp")
    private OffsetDateTime createdTimestamp;

    @NotNull
    @Column(name = "updated_timestamp")
    private OffsetDateTime updatedTimestamp;

    @Version
    @Column(name = "version")
    private Long version;
} 
package com.eaglebank.eaglebank_api.model;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @Pattern(regexp = "^tan-[A-Za-z0-9]+$")
    private String id;

    @NotNull
    @Pattern(regexp = "^01\\d{6}$")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "10000.00", inclusive = true)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String reference;

    @NotNull
    @Column(name = "created_timestamp")
    private OffsetDateTime createdTimestamp;
} 
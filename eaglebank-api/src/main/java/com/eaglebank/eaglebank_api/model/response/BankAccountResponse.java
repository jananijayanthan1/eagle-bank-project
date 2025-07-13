package com.eaglebank.eaglebank_api.model.response;

import java.time.OffsetDateTime;

import com.eaglebank.eaglebank_api.enums.AccountType;
import com.eaglebank.eaglebank_api.enums.Currency;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankAccountResponse {

    @NotNull
    @Pattern(regexp = "^01\\d{6}$")
    private String accountNumber;

    @NotNull
    private String sortCode;

    @NotNull
    private String name;

    @NotNull
    private AccountType accountType;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Balance must be at most 10000.00")
    private Double balance;

    @NotNull
    private Currency currency;

    @NotNull
    private OffsetDateTime createdTimestamp;

    @NotNull
    private OffsetDateTime updatedTimestamp;


    
}

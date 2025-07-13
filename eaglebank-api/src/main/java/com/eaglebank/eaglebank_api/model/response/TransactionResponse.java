package com.eaglebank.eaglebank_api.model.response;

import java.time.OffsetDateTime;

import com.eaglebank.eaglebank_api.enums.Currency;
import com.eaglebank.eaglebank_api.enums.TransactionType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TransactionResponse {

    @NotNull
    @Pattern(regexp = "^tan-[A-Za-z0-9]+$")
    private String id;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Balance must be at most 10000.00")
    private Double amount;

    @NotNull
    private Currency currency;

    @NotNull
    private TransactionType type;

    @NotNull
    private String reference;

    @NotNull
    @Pattern(regexp = "^usr-[A-Za-z0-9]+$")
    private String userId;

    @NotNull
    private OffsetDateTime createdTimestamp;
    

}

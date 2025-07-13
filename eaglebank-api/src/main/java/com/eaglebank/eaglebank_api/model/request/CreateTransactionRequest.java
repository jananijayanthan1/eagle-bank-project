package com.eaglebank.eaglebank_api.model.request;

import java.util.Currency;

import com.eaglebank.eaglebank_api.enums.TransactionType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTransactionRequest {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Balance must be at most 10000.00")
    private Double amount;
    
    @NotNull
    private Currency currency;

    @NotNull
    private TransactionType type;
    
}

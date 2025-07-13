package com.eaglebank.eaglebank_api.model.request;


import com.eaglebank.eaglebank_api.enums.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBankAccountRequest {

    @NotNull
    private String name;

    @NotNull
    private AccountType accountType;
    
}

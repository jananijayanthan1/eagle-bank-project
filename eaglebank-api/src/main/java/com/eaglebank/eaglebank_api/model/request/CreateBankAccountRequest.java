package com.eaglebank.eaglebank_api.model.request;

import com.eaglebank.eaglebank_api.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBankAccountRequest {

    @NotBlank
    private String name;

    @NotNull
    private AccountType accountType;

}

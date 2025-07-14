package com.eaglebank.eaglebank_api.mapper;

import com.eaglebank.eaglebank_api.model.BankAccount;
import com.eaglebank.eaglebank_api.model.response.BankAccountResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountMapper {
    
    public BankAccountResponse toResponse(BankAccount bankAccount) {
        BankAccountResponse response = new BankAccountResponse();
        response.setAccountNumber(bankAccount.getAccountNumber());
        response.setSortCode(bankAccount.getSortCode());
        response.setName(bankAccount.getName());
        response.setAccountType(bankAccount.getAccountType());
        response.setBalance(bankAccount.getBalance());
        response.setCurrency(bankAccount.getCurrency());
        response.setCreatedTimestamp(bankAccount.getCreatedTimestamp());
        response.setUpdatedTimestamp(bankAccount.getUpdatedTimestamp());
        return response;
    }

    public List<BankAccountResponse> toResponseList(List<BankAccount> bankAccounts) {
        return bankAccounts.stream().map(this::toResponse).toList();
    }
} 
package com.eaglebank.eaglebank_api.model.response;

import java.util.List;

import lombok.Data;

@Data
public class ListTransactionsResponse {

    private List<TransactionResponse> transactions;

}

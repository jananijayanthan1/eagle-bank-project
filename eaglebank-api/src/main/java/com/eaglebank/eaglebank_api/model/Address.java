package com.eaglebank.eaglebank_api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Address {

    @NotNull
    private String line1;

    @NotNull
    private String line2;

    @NotNull
    private String line3;

    @NotNull
    private String town;

    @NotNull
    private String county;

    @NotNull
    private String postcode;
}

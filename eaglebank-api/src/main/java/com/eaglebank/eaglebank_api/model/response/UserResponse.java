package com.eaglebank.eaglebank_api.model.response;

import java.time.OffsetDateTime;

import com.eaglebank.eaglebank_api.model.Address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserResponse {

    @NotNull
    @Pattern(regexp = "^usr-[A-Za-z0-9]+$")
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Address address;

    @NotNull
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    private String phoneNumber;

    @NotNull
    @Email
    private String email;

    @NotNull
    private OffsetDateTime createdTimestamp;    

    @NotNull
    private OffsetDateTime updatedTimestamp;
    


}

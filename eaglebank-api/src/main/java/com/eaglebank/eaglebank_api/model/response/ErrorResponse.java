package com.eaglebank.eaglebank_api.model.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ErrorResponse {

    private String error;
    private String message;
    private int status;
    private OffsetDateTime timestamp;
    private String path;
    
}

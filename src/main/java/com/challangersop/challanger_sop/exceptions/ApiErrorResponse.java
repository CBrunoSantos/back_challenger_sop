package com.challangersop.challanger_sop.exceptions;

import java.time.OffsetDateTime;

public class ApiErrorResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public ApiErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public OffsetDateTime getTimestamp() {
        return this.timestamp;
    }
}

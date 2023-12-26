package com.achrafaitibba.trackcompoundingtrades.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(
        String message,
        HttpStatus httpStatus,
        Integer httpStatusNumber,
        ZonedDateTime timestamp
)
{
    public ApiException(String message,
                        HttpStatus httpStatus,
                        Integer httpStatusNumber,
                        ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatusNumber = httpStatusNumber;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;

    }
}

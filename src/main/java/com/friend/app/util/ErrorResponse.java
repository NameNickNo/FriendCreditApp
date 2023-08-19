package com.friend.app.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String timestamp;

    public ErrorResponse(HttpStatus httpStatus, String message, String timestamp) {
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}

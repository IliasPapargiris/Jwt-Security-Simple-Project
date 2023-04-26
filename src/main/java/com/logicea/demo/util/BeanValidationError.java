package com.logicea.demo.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
class BeanValidationError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    BeanValidationError(HttpStatus status, String message, Throwable ex) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }


}
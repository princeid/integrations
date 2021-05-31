package com.prince.integrations.providers.interswitch.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private String error;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp timestamp;
    private T data;

    private ApiResponse(){
        timestamp = Timestamp.valueOf(LocalDateTime.now());
    }

    public ApiResponse(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiResponse(HttpStatus status, String message, T data) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

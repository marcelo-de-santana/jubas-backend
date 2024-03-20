package com.jubasbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String path;

    public ErrorResponse(String message){
        this.error = message;
    }
}

package com.jubasbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException {

    private final HttpStatus status;

    public APIException(final HttpStatus status, final ErrorResponse error, final Throwable cause) {
        super(error.getError(), cause);
        this.status = status;
    }

    public APIException(final HttpStatus status, final String message, final Throwable cause) {
        this(status, new ErrorResponse(message), cause);
    }

    public APIException(final HttpStatus status, final String message) {
        this(status, new ErrorResponse(message), null);
    }

}

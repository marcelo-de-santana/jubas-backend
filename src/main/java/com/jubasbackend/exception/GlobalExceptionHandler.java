package com.jubasbackend.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleAPIException(APIException exception, HttpServletRequest request) {
        return response(exception.getStatus(), exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleWrongArguments(IllegalArgumentException exception, HttpServletRequest request) {
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException exception, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String errorMessage, HttpServletRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(new Date(), status.value(), errorMessage, request.getRequestURI()));
    }

}

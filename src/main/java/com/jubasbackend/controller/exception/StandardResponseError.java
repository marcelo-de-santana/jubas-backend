package com.jubasbackend.controller.exception;

import java.io.Serializable;
import java.time.Instant;

public class StandardResponseError implements Serializable {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public StandardResponseError(Integer status, String error, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

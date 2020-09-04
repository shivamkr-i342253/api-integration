package com.teikametrics.apiintegration.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationAbortedException extends Throwable {

    private HttpStatus status;

    private String reason;

    public AuthorizationAbortedException(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

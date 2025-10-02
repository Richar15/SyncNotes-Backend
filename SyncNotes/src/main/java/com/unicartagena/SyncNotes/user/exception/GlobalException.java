package com.unicartagena.SyncNotes.user.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {
    private final HttpStatus status;

    public GlobalException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public GlobalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

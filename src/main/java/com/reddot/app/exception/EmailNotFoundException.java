package com.reddot.app.exception;

public class EmailNotFoundException extends ResourceNotFoundException {

    private static final String message = "Email not found exception";

    public EmailNotFoundException() {
        super(message);
    }

    public EmailNotFoundException(String msg) {
        super(msg);
    }

    public EmailNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

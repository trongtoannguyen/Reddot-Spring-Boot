package com.reddot.app.exception;

import java.io.Serial;

public class UserNotFoundException extends ResourceNotFoundException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String message = "User not found exception";

    public UserNotFoundException() {
        super(message);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

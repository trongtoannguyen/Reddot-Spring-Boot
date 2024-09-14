package com.reddot.app.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class ErrorObject {
    private int statusCode;
    private String message;
    private String path;
    private String timestamp = Instant.now().toString();

    public ErrorObject(int statusCode, String message, String path) {
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;
    }
}

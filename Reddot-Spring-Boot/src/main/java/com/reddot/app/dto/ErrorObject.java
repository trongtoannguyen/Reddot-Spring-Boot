package com.reddot.app.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
//set snake case for properties in the json response
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
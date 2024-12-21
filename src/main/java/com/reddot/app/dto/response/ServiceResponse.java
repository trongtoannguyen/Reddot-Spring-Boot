package com.reddot.app.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@code ServiceResponse} is a generic class used to wrap all successful responses
 * to ensure that the response is consistent and easy to understand.
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServiceResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ServiceResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

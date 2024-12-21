package com.reddot.app.dto.request;

import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.util.Set;

/**
 * QuestionCreateDTO record is used to create a new question.
 *
 * @param title
 * @param body
 * @param tags
 */
public record QuestionCreateDTO(

        @NonNull
        @Size(min = 3, max = 100)
        String title,
        @NonNull
        String body, Set<String> tags) {
}
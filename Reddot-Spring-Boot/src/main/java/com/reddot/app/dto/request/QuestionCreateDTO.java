package com.reddot.app.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO for creating a question
 * Contains the title, body and at most 5 tags (can be empty)
 */
@Data
public class QuestionCreateDTO {
    @NonNull
    @Size(min = 3, max = 100)
    String title;
    @NonNull
    String body;
    Set<String> tags = new HashSet<>();
}
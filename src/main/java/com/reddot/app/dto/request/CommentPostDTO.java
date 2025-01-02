package com.reddot.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

/**
 * This class represents the data transfer object for creating a new comment.
 * <p>
 * {@code id} is the id of the question or comment based on the context.
 */
@Data
public class CommentPostDTO {
    @JsonIgnore
    Integer id;
    @NonNull
    String body;
}

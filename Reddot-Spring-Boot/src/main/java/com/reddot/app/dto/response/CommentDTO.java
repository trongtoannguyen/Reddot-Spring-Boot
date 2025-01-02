package com.reddot.app.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Integer commentId;
    private String text;
    private Integer questionId;
    private Integer responseTo;
    private ShallowUserDTO author;
    private LocalDateTime creationDate;
    private LocalDateTime lastEditDate;
    private int upvotes;
    private Boolean upvoted;
    private int downvotes;
    private Boolean downvoted;
    private int score;
}
package com.reddot.app.service.comment;

import org.springframework.stereotype.Component;

@Component
public interface CommentService {
    Boolean isCommentUpvotedByUser(Integer commentId, Integer userId);

    Boolean isCommentDownvotedByUser(Integer commentId, Integer userId);
}

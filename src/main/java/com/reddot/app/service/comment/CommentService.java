package com.reddot.app.service.comment;

import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public interface CommentService {

    CommentDTO commentGetById(Integer commentId) throws ResourceNotFoundException;

    /**
     * This method is used to get a comment by its id.
     * Auth required to get a comment.
     *
     * @return This method returns the comment.
     * @throws ResourceNotFoundException if the comment is not found
     */
    CommentDTO commentGetWithUser(Integer commentId, Integer userId) throws ResourceNotFoundException;

    /**
     * Create a new comment on the given question.
     * Auth required to create a new comment.
     *
     * @return This method returns the created comment.
     * @throws ResourceNotFoundException if the user or question is not found
     */
    CommentDTO commentCreateOnQuestion(Integer userId, Integer questionId, String body) throws ResourceNotFoundException;

    Boolean isCommentUpvotedByUser(Integer commentId, Integer userId);

    Boolean isCommentDownvotedByUser(Integer commentId, Integer userId);
}

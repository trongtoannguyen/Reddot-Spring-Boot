package com.reddot.app.service.comment;

import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImp(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Boolean isCommentUpvotedByUser(Integer commentId, Integer userId) {
        return commentRepository.existsByIdAndVotes_UserIdAndVotes_VoteTypeId(commentId, userId, VOTETYPE.UPVOTE.getDirection());
    }

    @Override
    public Boolean isCommentDownvotedByUser(Integer commentId, Integer userId) {
        return commentRepository.existsByIdAndVotes_UserIdAndVotes_VoteTypeId(commentId, userId, VOTETYPE.DOWNVOTE.getDirection());
    }
}

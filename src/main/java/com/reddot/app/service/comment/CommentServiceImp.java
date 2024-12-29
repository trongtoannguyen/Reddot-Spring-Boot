package com.reddot.app.service.comment;

import com.reddot.app.assembler.CommentAssembler;
import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.entity.Comment;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.CommentRepository;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final CommentAssembler commentAssembler;

    @Override
    public CommentDTO commentGetById(Integer commentId) throws ResourceNotFoundException {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + commentId + " not found"));
            return commentAssembler.toDTO(comment);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentDTO commentGetWithUser(Integer commentId, Integer userId) throws ResourceNotFoundException {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + commentId + " not found"));
            CommentDTO dto = commentAssembler.toDTO(comment);

            // custom logic for user-specific properties
            if (userId != null) {
                dto.setUpvoted(isCommentUpvotedByUser(commentId, userId));
                dto.setDownvoted(isCommentDownvotedByUser(commentId, userId));
            }
            return dto;
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentDTO commentCreateOnQuestion(Integer userId, Integer questionId, String body) throws ResourceNotFoundException {
        try {
            User author = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
            Comment comment = new Comment(body, author);
            question.addComment(comment);
            commentRepository.save(comment);
            return commentAssembler.toDTO(comment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

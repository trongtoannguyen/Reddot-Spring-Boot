package com.reddot.app.service.question;

import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public interface QuestionService {
    /**
     * Create a new question
     *
     * @param userId the id of the user creating the question
     * @param dto    QuestionCreateDTO object containing the question details
     * @return QuestionDTO object containing the created question details
     * @throws ResourceNotFoundException if the user is not found
     */
    QuestionDTO questionCreate(Integer userId, QuestionCreateDTO dto) throws ResourceNotFoundException;

    /**
     * This method invoked by request contains an authentication access token.
     * Method returns some user-specific properties related to the question.
     *
     * @param questionId the id of the question
     * @param userId     the id of the user
     * @return QuestionDTO object containing the question details
     * @throws ResourceNotFoundException if the question is not found
     */
    QuestionDTO questionDetailGetById(Integer questionId, Integer userId) throws ResourceNotFoundException;

    /**
     * This method is used to get a question by its id.
     *
     * @param id the id of the question
     * @return QuestionDTO object containing the question details
     * @throws ResourceNotFoundException if the question is not found
     */
    // todo: https://api.stackexchange.com/docs/questions-by-ids
    QuestionDTO questionGetById(Integer id) throws ResourceNotFoundException;

    boolean isQuestionUpvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionDownvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionBookmarkedByUser(Integer questionId, Integer userId);
}

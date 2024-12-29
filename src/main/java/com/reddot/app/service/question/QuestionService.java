package com.reddot.app.service.question;

import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.exception.BadRequestException;
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
    QuestionDTO questionGetWithUser(Integer questionId, Integer userId) throws ResourceNotFoundException;

    /**
     * This method is used to get a question by its id.
     *
     * @param id the id of the question
     * @return QuestionDTO object containing the question details
     * @throws ResourceNotFoundException if the question is not found
     */
    // todo: https://api.stackexchange.com/docs/questions-by-ids
    QuestionDTO questionGetById(Integer id) throws ResourceNotFoundException;

    /**
     * Deletes a question.
     * <p>
     * Note that only question owners can delete their questions, this is not equivalent to casting a delete vote.
     * Also be aware that there are conditions when even a question's owner cannot delete it, and this method will respect those limits.
     * <p>
     * Use an access_token to delete a question.
     * <p>
     * It is not possible to undelete a question.
     * <p>
     * In practice, this method will never return an object.
     *
     * @param questionId the id of the question
     * @param userId     the id of the user
     * @throws ResourceNotFoundException if the question is not found
     * @throws BadRequestException       if the user is not permitted to delete the question
     */
    void questionDelete(Integer questionId, Integer userId) throws ResourceNotFoundException, BadRequestException;

    boolean isQuestionUpvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionDownvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionBookmarkedByUser(Integer questionId, Integer userId);
}

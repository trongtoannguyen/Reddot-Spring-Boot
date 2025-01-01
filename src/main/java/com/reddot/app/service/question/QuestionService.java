package com.reddot.app.service.question;

import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.request.QuestionUpdateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.User;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO: implement sorting and filtering
@Component
public interface QuestionService {
    /**
     * Create a new question
     *
     * @param user the id of the user creating the question
     * @param dto  QuestionPostDTO object containing the question details
     * @return QuestionDTO object containing the created question details
     * @throws ResourceNotFoundException if the user is not found
     */
    QuestionDTO questionCreate(User user, QuestionCreateDTO dto) throws ResourceNotFoundException;

    /**
     * Get the question identified by the given id.
     *
     * @param id the id of the question
     * @return QuestionDTO object containing the question details
     * @throws ResourceNotFoundException if the question is not found
     */
    // todo: https://api.stackexchange.com/docs/questions-by-ids
    QuestionDTO questionGetById(Integer id) throws ResourceNotFoundException;

    /**
     * Get the question identified by the given id.
     * Method returns some user-specific properties related to the question.
     *
     * @param questionId the id of the question
     * @param user       the user requesting the question
     * @return QuestionDTO object containing the question details
     * @throws ResourceNotFoundException if the question is not found
     */
    QuestionDTO questionGetWithUser(Integer questionId, @NonNull User user) throws ResourceNotFoundException;

    /**
     * Get all questions on the site.
     *
     * @return List of QuestionDTO objects containing the question details
     */
    List<QuestionDTO> questionGetAll();

    /**
     * Get all questions on the site with user id.
     * Method returns some user-specific properties related to the questions.
     *
     * @param user the user requesting the questions
     * @return List of QuestionDTO objects containing the question details
     * @throws ResourceNotFoundException if the user is not found
     */
    List<QuestionDTO> questionGetAllWithUser(@NonNull User user) throws ResourceNotFoundException;

    /**
     * TODO: should implement review by mod before updating
     * Edits the given question.
     *
     * @param user the user requesting the question update
     * @param dto  QuestionPostDTO object containing the new question details
     * @return the updated question.
     * @throws ResourceNotFoundException if the question is not found
     * @throws BadRequestException       if the user is not permitted to edit the question
     */
    QuestionDTO questionUpdate(User user, QuestionUpdateDTO dto) throws ResourceNotFoundException, BadRequestException;

    /**
     * @param questionId the id of the question
     * @param user       the user requesting the question
     * @throws ResourceNotFoundException if the question is not found
     * @throws BadRequestException       if the user is not permitted to delete the question
     */
    void questionDelete(Integer questionId, User user) throws ResourceNotFoundException, BadRequestException;

    boolean isQuestionUpvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionDownvotedByUser(Integer questionId, Integer userId);

    boolean isQuestionBookmarkedByUser(Integer questionId, Integer userId);
}

package com.reddot.app.controller;

import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.request.QuestionUpdateDTO;
import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.comment.CommentService;
import com.reddot.app.service.question.QuestionService;
import com.reddot.app.service.system.SystemAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final CommentService commentService;

    @Operation(summary = "Creates a comment on the given question. [auth required] ",
            description = """
                    In the API, any "low quality" checks that are triggered cause the write request to fail.
                    This includes situations where a CAPTCHA or guidance text would be displayed on the websites.
                    
                    Use an access_token with role_user to create a new comment.
                    
                    This method returns the question with the new comment.
                    """)
    @PostMapping("/{id}/comments/add")
    public ResponseEntity<ServiceResponse<CommentDTO>> addComment(@PathVariable Integer id, String body) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            CommentDTO commentDTO = commentService.commentCreateOnQuestion(user.getId(), id, body);
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question retrieved successfully", commentDTO));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Operation(summary = "Create a new question", description = """
            In the API, any "low quality" checks that are triggered cause the write request to fail.
            This includes situations where a CAPTCHA or guidance text would be displayed on the websites.
            
            Use an access_token with role_user to create a new question.
            
            This method returns the created question.""")
    @PostMapping("/add")
    public ResponseEntity<ServiceResponse<QuestionDTO>> createQuestion(@Valid @RequestBody QuestionCreateDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!SystemAuthentication.isLoggedIn(authentication)) {
                throw new BadRequestException("You must be logged in to create a question");
            }
            User user = (User) authentication.getPrincipal();
            QuestionDTO questionDTO = questionService.questionCreate(user.getId(), dto);
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question created successfully", questionDTO));
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Gets all the questions on the site.",
            description = """
                    Use this method to retrieve all questions include some user-specific properties related to the questions.
                    
                    This method returns a list of questions.""")
    @GetMapping
    public ResponseEntity<ServiceResponse<List<QuestionDTO>>> getAllQuestion() {
        try {
            Integer userId;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<QuestionDTO> list;
            if (SystemAuthentication.isLoggedIn(authentication)) {
                User user = (User) authentication.getPrincipal();
                userId = user.getId();
                list = questionService.questionGetAllWithUser(userId);
            } else {
                list = questionService.getAllQuestions();
            }
            return ResponseEntity.ok(new ServiceResponse<>(200, "Questions retrieved successfully", list));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Operation(summary = "Returns the question identified in {id}.",
            description = """
                    Use this method to retrieve a question by its ID include some user-specific properties related to the question.
                    
                    This method returns the question.""")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<QuestionDTO>> getQuestion(@PathVariable Integer id) {
        try {
            Integer userId;
            QuestionDTO questionDTO;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (SystemAuthentication.isLoggedIn(authentication)) {
                User user = (User) authentication.getPrincipal();
                userId = user.getId();
                questionDTO = questionService.questionGetWithUser(id, userId);
            } else {
                questionDTO = questionService.questionGetById(id);
            }
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question retrieved successfully", questionDTO));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Operation(summary = "Edit an existing question. [auth required]",
            description = """
                    Use this method to edit a question identified by its ID.
                    
                    This method returns the updated question.""")
    @PutMapping("/{id}/update")
    public ResponseEntity<ServiceResponse<QuestionDTO>> updateQuestion(@PathVariable Integer id, @RequestBody QuestionUpdateDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            dto.setId(id);
            QuestionDTO questionDTO = questionService.questionUpdate(user.getId(), dto);
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question updated successfully", questionDTO));
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Deletes the question identified in {id}. [auth required]",
            description = """
                    Note that only question owners can delete their questions, this is not equivalent to casting a delete vote.
                    Also be aware that there are conditions when even a question's owner cannot delete it, and this method will respect those limits.
                    
                    Use an access_token to delete a question.
                    
                    It is not possible to undelete a question.
                    
                    In practice, this method will never return an object.
                    """)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ServiceResponse<String>> deleteQuestion(@PathVariable Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            questionService.questionDelete(id, user.getId());
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question deleted successfully", "Question deleted successfully"));
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/search")
    public List<QuestionDTO> searchQuestions(
            @RequestParam(value = "content" , required = false)String content,
            @RequestParam(value = "displayName",required = false)String displayName){

        if(content != null && !content.isBlank()){
            return questionService.searchByKeyword(content);
        } else if(displayName != null && !displayName.isBlank()){
            return questionService.searchByDisplayName(displayName);
        }else {
            return questionService.getAllQuestions();
        }
    }

    @PutMapping("/questions/{questionId}/visibility")
    public ResponseEntity<QuestionDTO> togggleQuestionVisibility(@PathVariable Integer questionId,@RequestParam Integer loggedInUserId) throws  ResourceNotFoundException, BadRequestException {
        QuestionDTO updatedQuestion = questionService.toggleVisibiliy(questionId,loggedInUserId);

        return ResponseEntity.ok(updatedQuestion);
    }
}
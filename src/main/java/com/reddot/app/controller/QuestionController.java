package com.reddot.app.controller;

import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.question.QuestionService;
import com.reddot.app.service.system.SystemAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "Create a new question", description = """
            In the API, any "low quality" checks that are triggered cause the write request to fail.
            This includes situations where a CAPTCHA or guidance text would be displayed on the websites.
                       \s
            Use an access_token with role_user to create a new question.
                       \s
            This method returns the created question.""")
    @PostMapping("/add")
    public ResponseEntity<ServiceResponse<QuestionDTO>> createQuestion(@Valid @RequestBody QuestionCreateDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            QuestionDTO questionDTO = questionService.questionCreate(user.getId(), dto);
            return ResponseEntity.ok(new ServiceResponse<>(200, "Question created successfully", questionDTO));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Returns the question identified in {id}.",
            description = """
                    Use this method to retrieve a question by its ID.
                    This method returns the question.""")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<QuestionDTO>> getQuestion(@PathVariable Integer id) {
        try {
            Integer userId;
            QuestionDTO questionDTO;
            if (SystemAuthentication.isLoggedIn()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
}
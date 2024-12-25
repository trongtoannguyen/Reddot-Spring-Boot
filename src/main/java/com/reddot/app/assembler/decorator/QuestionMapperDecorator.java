package com.reddot.app.assembler.decorator;

import com.reddot.app.assembler.QuestionAssembler;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.User;
import com.reddot.app.service.question.QuestionService;
import com.reddot.app.service.system.SystemAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class QuestionMapperDecorator implements QuestionAssembler {

    @Autowired
    @Qualifier("delegate")
    private QuestionAssembler delegate;
    @Autowired
    @Lazy
    private QuestionService questionService;

    @Override
    public QuestionDTO toDTO(Question question) {
        QuestionDTO dto = delegate.toDTO(question);
        dto.setUpvoted(isQuestionUpvoted(question));
        dto.setDownvoted(isQuestionDownvoted(question));
        dto.setBookmarked(isQuestionBookmark(question));
        return dto;
    }

    private Boolean isQuestionUpvoted(Question question) {
        if (SystemAuthentication.isLoggedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Integer userId = user.getId();
            return questionService.isQuestionUpvotedByUser(question.getId(), userId);
        }
        return null;
    }

    private Boolean isQuestionDownvoted(Question question) {
        if (SystemAuthentication.isLoggedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Integer userId = user.getId();
            return questionService.isQuestionDownvotedByUser(question.getId(), userId);
        }
        return null;
    }

    private Boolean isQuestionBookmark(Question question) {
        if (SystemAuthentication.isLoggedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Integer userId = user.getId();
            return questionService.isQuestionBookmarkedByUser(question.getId(), userId);
        }
        return null;
    }
}


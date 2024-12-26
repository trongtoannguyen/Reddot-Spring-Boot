package com.reddot.app.controller;

import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.comment.CommentService;
import com.reddot.app.service.system.SystemAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<CommentDTO>> getComment(@PathVariable Integer id) {
        try {

            CommentDTO dto;
            Integer userId;
            if (SystemAuthentication.isLoggedIn()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                userId = user.getId();
                dto = commentService.commentGetWithUser(id, userId);
            } else {
                dto = commentService.commentGetById(id);
            }
            return ResponseEntity.ok(new ServiceResponse<>(200, "Comment retrieved successfully", dto));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

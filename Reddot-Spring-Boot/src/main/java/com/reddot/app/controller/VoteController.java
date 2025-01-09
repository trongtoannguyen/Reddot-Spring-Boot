package com.reddot.app.controller;

import com.reddot.app.dto.request.VoteDto;
import com.reddot.app.service.vote.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // API để vote (toggle vote)
    @PostMapping("/{id}/vote")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> voteQuestion(
            @PathVariable("id") Integer questionId,
            @RequestBody VoteDto voteDto,
            Authentication auth) {
        String username = auth.getName();
        voteService.voteQuestion(questionId, voteDto, username);
        return ResponseEntity.ok("Vote processed successfully");
    }
}

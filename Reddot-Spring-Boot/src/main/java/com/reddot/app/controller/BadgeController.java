package com.reddot.app.controller;


import com.reddot.app.service.badges.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkBadges(Authentication auth) {
        String username = auth.getName();
        badgeService.checkAndAwardBadges(username);
        return ResponseEntity.ok("Badge check completed.");
    }
}

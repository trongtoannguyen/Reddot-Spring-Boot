package com.reddot.app.service.badges;

import com.reddot.app.service.user.StatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BadgeScheduler {
    private final StatisticsService statisticsService;

    public BadgeScheduler(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void assignBadgesAutomatically() {
        try {
            statisticsService.assignBadgesToAllUsers(); // Logic gán badge
            System.out.println("Badges assigned successfully at " + LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Error assigning badges: " + e.getMessage());
        }
    }
}

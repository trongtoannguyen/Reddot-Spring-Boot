package com.reddot.app.controller;

import com.reddot.app.dto.response.UserAnswerStatisticsDTO;
import com.reddot.app.dto.response.UserBadgeStatisticsDTO;
import com.reddot.app.dto.response.UserFollowerStatisticsDTO;
import com.reddot.app.dto.response.UserQuestionStatisticsDTO;
import com.reddot.app.entity.Badge;
import com.reddot.app.service.user.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics/")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{userId}/questions")
    public UserQuestionStatisticsDTO getUserQuestions(@PathVariable Integer userId) {
        return statisticsService.getUserQuestionStatistics(userId);
    }

    @GetMapping("/{userId}/answers")
    public UserAnswerStatisticsDTO getUserAnswers(@PathVariable Integer userId) {
        return statisticsService.getUserAnswerStatistics(userId);
    }

    @GetMapping("/{userId}/question-upvotes")
    public ResponseEntity<Long> countQuestionUpvotes(@PathVariable Integer userId) {
        return ResponseEntity.ok(statisticsService.countUpvotesForQuestionsByUserId(userId));
    }

    @GetMapping("/{userId}/question-downvotes")
    public ResponseEntity<Long> countQuestionDownvotes(@PathVariable Integer userId) {
        return ResponseEntity.ok(statisticsService.countDownvotesForQuestionsByUserId(userId));
    }

    @GetMapping("/{userId}/comment-upvotes")
    public ResponseEntity<Long> countCommentUpvotes(@PathVariable Integer userId) {
        return ResponseEntity.ok(statisticsService.countUpvotesForCommentsByUserId(userId));
    }

    @GetMapping("/{userId}/comment-downvotes")
    public ResponseEntity<Long> countCommentDownvotes(@PathVariable Integer userId) {
        return ResponseEntity.ok(statisticsService.countDownvotesForCommentsByUserId(userId));
    }

    @GetMapping("/{userId}/totalBadges")
    public UserBadgeStatisticsDTO getUserTotalBadges(@PathVariable Integer userId) {
        return statisticsService.getUserBadgeStatistics(userId);
    }

    @GetMapping("/{userId}/listBadgesByUserId")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable Integer userId) {
        List<Badge> badges = statisticsService.getBadgesByUserId(userId);
        return ResponseEntity.ok(badges);
    }


    @GetMapping("/{userId}/followers")
    public UserFollowerStatisticsDTO getUserFollowers(@PathVariable Integer userId) {
        return statisticsService.getUserFollowerStatistics(userId);
    }

    @GetMapping("/getTopTags/{userId}")
    public List<String> getTopTags(@PathVariable Integer userId) {
        return statisticsService.getTopTagsByUserId(userId);
    }

    @GetMapping("/topCommnets/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTopCommentsWithQuestions(@PathVariable Integer userId) {
        List<Map<String, Object>> topComments = statisticsService.getTopCommentsWithQuestionsByUserId(userId);
        return ResponseEntity.ok(topComments);
    }

    @GetMapping("/topQuestions/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTopQuestions(@PathVariable Integer userId) {
        List<Map<String, Object>> topQuestions = statisticsService.getTopQuestionsByVotesForUser(userId);
        return ResponseEntity.ok(topQuestions);
    }

    //Tag trending
    @GetMapping("/tag-trending")
    public ResponseEntity<List<Map<String, Object>>> getTrendingTags() {
        return ResponseEntity.ok(statisticsService.getTrendingTags());
    }

    @GetMapping("/assign-badges/{userId}")
    public String assignBadges(@PathVariable Integer userId) {
        try {
            statisticsService.assignBadgesToUser(userId);
            return "Badges assigned successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}

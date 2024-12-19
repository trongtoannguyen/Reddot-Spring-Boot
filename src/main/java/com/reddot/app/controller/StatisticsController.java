package com.reddot.app.controller;

import com.reddot.app.dto.*;
import com.reddot.app.dto.response.*;
import com.reddot.app.entity.Badge;
import com.reddot.app.service.user.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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



    @GetMapping("/assign-badges/{userId}")
    public String assignBadges(@PathVariable Integer userId) {
        try {
            statisticsService.assignBadgesToUser(userId);
            return "Badges assigned successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    //Thông kê toàn bộ web
    //Tag trending
    @GetMapping("/tag-trending")
    public ResponseEntity<List<Map<String, Object>>> getTrendingTags() {
        return ResponseEntity.ok(statisticsService.getTrendingTags());
    }

    //Total user
    @GetMapping("/count-user")
    public Map<String, Long> getTotalUsers() {
        return Map.of("totalUsers", statisticsService.getTotalUsers());
    }

    //Thống kê câu hỏi theo ngày , theo tháng, theo năm
    // Tổng số câu hỏi theo ngày
    @GetMapping("/count-question-by-day")
    public Map<String, Long> getQuestionsByDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long total = statisticsService.getTotalQuestionsByDay(date);
        return Map.of("totalQuestionsByDay", total);
    }

    // Tổng số câu hỏi theo tuần
    @GetMapping("/count-question-by-week")
    public Map<String, Long> getQuestionsByWeek(@RequestParam("year") int year, @RequestParam("week") int week) {
        long total = statisticsService.getTotalQuestionsByWeek(year, week);
        return Map.of("totalQuestionsByWeek", total);
    }

    // Tổng số câu hỏi theo tháng
    @GetMapping("/count-question-by-month")
    public Map<String, Long> getQuestionsByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        long total = statisticsService.getTotalQuestionsByMonth(year, month);
        return Map.of("totalQuestionsByMonth", total);
    }

    // Tổng số câu hỏi theo năm
    @GetMapping("/count-question-by-year")
    public Map<String, Long> getQuestionsByYear(@RequestParam("year") int year) {
        long total = statisticsService.getTotalQuestionsByYear(year);
        return Map.of("totalQuestionsByYear", total);
    }

    //Thống kê comment theo nga, theo tháng , theo nam
    // Tổng số comment theo ngày
    @GetMapping("/count-comment-by-day")
    public Map<String, Long> getCommentsByDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long total = statisticsService.getTotalCommentsByDay(date);
        return Map.of("totalCommentsByDay", total);
    }

    // Tổng số comment theo tuần
    @GetMapping("/count-comment-by-week")
    public Map<String, Long> getCommentsByWeek(@RequestParam("year") int year, @RequestParam("week") int week) {
        long total = statisticsService.getTotalCommentsByWeek(year, week);
        return Map.of("totalCommentsByWeek", total);
    }

    // Tổng số comment theo tháng
    @GetMapping("/count-comment-by-month")
    public Map<String, Long> getCommentsByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        long total = statisticsService.getTotalCommentsByMonth(year, month);
        return Map.of("totalCommentsByMonth", total);
    }

    // Tổng số comment theo năm
    @GetMapping("/count-comment-by-year")
    public Map<String, Long> getCommentsByYear(@RequestParam("year") int year) {
        long total = statisticsService.getTotalCommentsByYear(year);
        return Map.of("totalCommentsByYear", total);
    }

    //Thng ke so nguoi dung moi theo ngay. thang, nam
    // Số người dùng mới theo ngày
    @GetMapping("/new-users-by-day")
    public Map<String, Long> getNewUsersByDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long total = statisticsService.getNewUsersByDay(date);
        return Map.of("newUsersByDay", total);
    }

    // Số người dùng mới theo tuần
    @GetMapping("/new-users-by-week")
    public Map<String, Long> getNewUsersByWeek(@RequestParam("year") int year, @RequestParam("week") int week) {
        long total = statisticsService.getNewUsersByWeek(year, week);
        return Map.of("newUsersByWeek", total);
    }

    // Số người dùng mới theo tháng
    @GetMapping("/new-users-by-month")
    public Map<String, Long> getNewUsersByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        long total = statisticsService.getNewUsersByMonth(year, month);
        return Map.of("newUsersByMonth", total);
    }

    // Số người dùng mới theo năm
    @GetMapping("/new-users-by-year")
    public Map<String, Long> getNewUsersByYear(@RequestParam("year") int year) {
        long total = statisticsService.getNewUsersByYear(year);
        return Map.of("newUsersByYear", total);
    }

    //lấy ra top câu hỏi dua tren tag
    @GetMapping("/top-question-by-tag")
    public ResponseEntity<List<TopQuestionByTagDTO>> getTopQuestionsByTag(@RequestParam String tagName) {
        List<TopQuestionByTagDTO> topQuestions = statisticsService.getTopQuestionsByTag(tagName);
        return ResponseEntity.ok(topQuestions);
    }
}

package com.reddot.app.service.user;

import com.reddot.app.dto.response.*;
import com.reddot.app.entity.Badge;
import com.reddot.app.entity.User;
import com.reddot.app.entity.UserBadge;
import com.reddot.app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserBadgeRepository UserBadgeRepository;
    private final FollowingRepository followingRepository;
    private final TagRepository tagRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    public StatisticsService(QuestionRepository questionRepository,
                             CommentRepository commentRepository,
                             VoteRepository voteRepository,
                             UserBadgeRepository UserBadgeRepository,
                             FollowingRepository followingRepository,
                             TagRepository tagRepository,
                             UserBadgeRepository userBadgeRepository,
                             UserRepository userRepository,
                             BadgeRepository badgeRepository) {
        this.questionRepository = questionRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.UserBadgeRepository = UserBadgeRepository;
        this.followingRepository = followingRepository;
        this.tagRepository = tagRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
    }

    public UserQuestionStatisticsDTO getUserQuestionStatistics(Integer userId) {
        Long totalQuestions = questionRepository.countQuestionsByUserId(userId);
        return new UserQuestionStatisticsDTO(totalQuestions);
    }

    public UserAnswerStatisticsDTO getUserAnswerStatistics(Integer userId) {
        Long totalAnswers = commentRepository.countAnswersByUserId(userId);
        return new UserAnswerStatisticsDTO(totalAnswers);
    }

    // Tổng số upvotes từ câu hỏi
    public Long countUpvotesForQuestionsByUserId(Integer userId) {
        Long result = voteRepository.countUpvotesForQuestionsByUserId(userId);
        return result != null ? result : 0L; // Nếu NULL thì trả về 0
    }

    // Tổng số downvotes từ câu hỏi
    public Long countDownvotesForQuestionsByUserId(Integer userId) {
        Long result = voteRepository.countDownvotesForQuestionsByUserId(userId);
        return result != null ? result : 0L; // Nếu NULL thì trả về 0
    }


    public Long countUpvotesForCommentsByUserId(Integer userId) {
        return voteRepository.countUpvotesForCommentsByUserId(userId);
    }

    public Long countDownvotesForCommentsByUserId(Integer userId) {
        return voteRepository.countDownvotesForCommentsByUserId(userId);
    }

    public UserBadgeStatisticsDTO getUserBadgeStatistics(Integer userId) {
        Long totalBadges = UserBadgeRepository.countBadgesByUserId(userId);
        return new UserBadgeStatisticsDTO(totalBadges);
    }

    public List<Badge> getBadgesByUserId(Integer userId) {
        return userBadgeRepository.findBadgesByUserId(userId);
    }

    public UserFollowerStatisticsDTO getUserFollowerStatistics(Integer userId) {
        Long totalFollowers = followingRepository.countFollowersByUserId(userId);
        return new UserFollowerStatisticsDTO(totalFollowers);
    }

    public List<String> getTopTagsByUserId(Integer userId) {
        List<Object[]> result = tagRepository.findTopTagsByUserId(userId);

        return result.stream()
                .map(row -> row[0] + " (used: " + row[1] + " times)")
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTopCommentsWithQuestionsByUserId(Integer userId) {
        Pageable topTen = PageRequest.of(0, 10);
        return commentRepository.findTopCommentsWithQuestionsByUserId(userId, topTen);
    }

    public List<Map<String, Object>> getTopQuestionsByVotesForUser(Integer userId) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> rawResults = questionRepository.findTopQuestionsByVotesForUser(userId, pageable);

        // Chuyển đổi Object[] thành Map với tên trường rõ ràng
        List<Map<String, Object>> formattedResults = new ArrayList<>();
        for (Object[] row : rawResults) {
            Map<String, Object> rowMap = new LinkedHashMap<>();
            rowMap.put("questionId", row[0]);
            rowMap.put("questionTitle", row[1]);
            rowMap.put("upvotes", row[2]);
            formattedResults.add(rowMap);
        }
        return formattedResults;
    }



    public void assignBadgesToAllUsers() {
        // Lấy danh sách tất cả người dùng
        List<User> allUsers = userRepository.findAll();

        // Lặp qua từng người dùng và gán badge nếu đạt điều kiện
        for (User user : allUsers) {
            Integer userId = user.getId();
            assignBadgesToUser(userId); // Sử dụng lại logic hiện tại
        }
    }

    public void assignBadgesToUser(Integer userId) {
        // Kiểm tra số lượng câu hỏi người dùng đã đăng
        long questionCount = questionRepository.countQuestionsByUserId(userId);


        // Kiểm tra số lượng bình luận người dùng đã đăng
        long commentCount = commentRepository.countAnswersByUserId(userId);

        // Kiểm tra số lượt upvote cho question, và comment
        Long questionUpvotes = voteRepository.countUpvotesForQuestionsByUserId(userId);
        if (questionUpvotes == null) {
            questionUpvotes = 0L; // Gán giá trị mặc định là 0
        }

        Long commentUpvotes = voteRepository.countUpvotesForCommentsByUserId(userId);
        if (commentUpvotes == null) {
            commentUpvotes = 0L;
        }

        // **Câu hỏi:**
        if (questionCount >= 100000000) {
            assignBadge(userId, "Legend"); // Người dùng đã đăng 100 triệu câu hỏi
        } else if (questionCount >= 10000000) {
            assignBadge(userId, "Veteran"); // Người dùng đã đăng 10 triệu câu hỏi
        } else if (questionCount >= 1000000) {
            assignBadge(userId, "Loyal"); // Người dùng đã đăng 1 triệu câu hỏi
        } else if (questionCount >= 100000) {
            assignBadge(userId, "Enthusiast"); // Người dùng đã đăng 100,000 câu hỏi
        } else if (questionCount >= 10000) {
            assignBadge(userId, "Crazy"); // Người dùng đã đăng 10,000 câu hỏi
        } else if (questionCount >= 1000) {
            assignBadge(userId, "Expert"); // Người dùng đã đăng 1,000 câu hỏi
        } else if (questionCount >= 100) {
            assignBadge(userId, "Advanced"); // Người dùng đã đăng 100 câu hỏi
        } else if (questionCount >= 10) {
            assignBadge(userId, "Intermediate"); // Người dùng đã đăng 10 câu hỏi
        } else if (questionCount >= 1) {
            assignBadge(userId, "Beginner"); // Người dùng đã đăng 1 câu hỏi
        }

        // **Bình luận:**
        if (commentUpvotes >= 1000) {
            assignBadge(userId, "Awesome Comment"); // Bình luận được upvote 1000 lần
        } else if (commentUpvotes >= 100) {
            assignBadge(userId, "Great Comment"); // Bình luận được upvote 100 lần
        } else if (commentUpvotes >= 10) {
            assignBadge(userId, "Good Comment"); // Bình luận được upvote 10 lần
        }

        // **Đóng góp chung:**
        if (questionUpvotes >= 1000) {
            assignBadge(userId, "Awesome Question"); // Câu hỏi được upvote 1000 lần
        } else if (questionUpvotes >= 100) {
            assignBadge(userId, "Great Question"); // Câu hỏi được upvote 100 lần
        } else if (questionUpvotes >= 10) {
            assignBadge(userId, "Good Question"); // Câu hỏi được upvote 10 lần
        }

        // **Điều kiện người dùng mới:**
        if (questionCount == 1) {
            assignBadge(userId, "Newbie"); // Người dùng đăng câu hỏi đầu tiên
        }
        if (commentCount == 1) {
            assignBadge(userId, "Beginner Commenter"); // Người dùng bình luận đầu tiên
        }
    }

    public void assignBadge(Integer userId, String badgeName) {
        // Tìm badge theo tên trong BadgeRepository
        Optional<Badge> badgeOptional = badgeRepository.findByName(badgeName);

        if (badgeOptional.isPresent()) {
            Badge badge = badgeOptional.get(); // Lấy Badge từ Optional

            // Kiểm tra xem người dùng đã nhận badge này chưa
            if (!userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId())) {
                // Tìm User dựa trên userId
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // Tạo đối tượng UserBadge mới và gán user và badge
                UserBadge userBadge = new UserBadge(user, badge);

                // Lưu UserBadge vào repository
                userBadgeRepository.save(userBadge);
            }
        } else {
            // Nếu badge không tồn tại trong cơ sở dữ liệu, ném lỗi hoặc xử lý tùy chọn
            throw new RuntimeException("Badge not found");
        }
    }

    //Thống ke chung

    //Tag trending
    public List<Map<String, Object>> getTrendingTags() {
        List<Object[]> results = tagRepository.findTrendingTags();

        return results.stream()
                .map(result -> Map.of(
                        "tagName", result[0],
                        "usageCount", result[1]
                ))
                .toList();
    }

    //Total user
    public long getTotalUsers() {
        return userRepository.count();
    }

    //Total question
    public long getTotalQuestions() {
        return questionRepository.count();
    }

    //Total comment
    public long getTotalComments() {
        return commentRepository.count();
    }

    //----------------

    //Thống kê câu hỏi theo ngày, theo tháng, theo năm
    public long getTotalQuestionsByDay(LocalDate date) {
        return questionRepository.countQuestionsByDay(date);
    }

    public long getTotalQuestionsByWeek(int year, int week) {
        return questionRepository.countQuestionsByWeek(year, week);
    }

    public long getTotalQuestionsByMonth(int year, int month) {
        return questionRepository.countQuestionsByMonth(year, month);
    }

    public long getTotalQuestionsByYear(int year) {
        return questionRepository.countQuestionsByYear(year);
    }

    //---------------
    //Thống kê comment theo ngày, theo tháng, theo năm
    public long getTotalCommentsByDay(LocalDate date) {
        return commentRepository.countCommentsByDay(date);
    }

    public long getTotalCommentsByWeek(int year, int week) {
        return commentRepository.countCommentsByWeek(year, week);
    }

    public long getTotalCommentsByMonth(int year, int month) {
        return commentRepository.countCommentsByMonth(year, month);
    }

    public long getTotalCommentsByYear(int year) {
        return commentRepository.countCommentsByYear(year);
    }

    //Thống kê user mới theo ngày , tháng , năm
    public long getNewUsersByDay(LocalDate date) {
        return userRepository.countNewUsersByDay(date);
    }

    public long getNewUsersByWeek(int year, int week) {
        return userRepository.countNewUsersByWeek(year, week);
    }

    public long getNewUsersByMonth(int year, int month) {
        return userRepository.countNewUsersByMonth(year, month);
    }

    public long getNewUsersByYear(int year) {
        return userRepository.countNewUsersByYear(year);
    }

    //Thong ke cau hoi hay dua theo tag
    public List<TopQuestionByTagDTO> getTopQuestionsByTag(String tagName) {
        List<Object[]> results = questionRepository.findTopQuestionsByTag(tagName);

        // Ánh xạ dữ liệu từ Object[] sang DTO
        return results.stream()
                .map(row -> new TopQuestionByTagDTO(
                        ((Number) row[0]).longValue(),    // questionId
                        (String) row[1],                 // questionTitle
                        ((Number) row[2]).longValue(),    // voteScore
                        (String) row[3])                 // tagName
                )
                .collect(Collectors.toList());
    }
}

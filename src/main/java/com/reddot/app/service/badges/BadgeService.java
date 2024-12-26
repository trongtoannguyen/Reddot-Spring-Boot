package com.reddot.app.service.badges;


import com.reddot.app.entity.Badge;
import com.reddot.app.entity.User;
import com.reddot.app.entity.UserBadge;
import com.reddot.app.repository.BadgeRepository;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.UserBadgeRepository;
import com.reddot.app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BadgeService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public BadgeService(UserRepository userRepository, QuestionRepository questionRepository,
                        BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    public void checkAndAwardBadges(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra số lượng câu hỏi
        Long questionCount = questionRepository.countQuestionsByUserId(user.getId());

        // Kiểm tra tổng số vote
        Long totalVotes = questionRepository.countUpvotesForQuestionsByUserId(user.getId());

        if (questionCount >= 5 && totalVotes > 10) {
            Badge badge = badgeRepository.findByName("Pro Questioner")
                    .orElseGet(() -> badgeRepository.save(new Badge("Pro Questioner", "badge_url", "Achieved for asking 5+ questions and getting 10+ votes", "Gold")));

            if (userBadgeRepository.findByUserAndBadge(user, badge).isEmpty()) {
                userBadgeRepository.save(new UserBadge(user, badge));
            }
        }
    }
}


package com.reddot.app.repository;

import com.reddot.app.entity.Badge;
import com.reddot.app.entity.User;
import com.reddot.app.entity.UserBadge;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
    @Query("SELECT COUNT(ub) FROM user_badges ub WHERE ub.user.id = :userId")
    Long countBadgesByUserId(@NonNull Integer userId);

    @Query("SELECT b FROM badges b " +
            "JOIN user_badges ub ON b.id = ub.badge.id " +
            "WHERE ub.user.id = :userId " +
            "ORDER BY b.tier ASC")
    List<Badge> findBadgesByUserId(@Param("userId") Integer userId);

    // Kiểm tra xem người dùng đã nhận badge này chưa
    boolean existsByUserIdAndBadgeId(Integer userId, Integer badgeId);


    // Thêm một badge mới cho người dùng
    UserBadge save(UserBadge userBadge);


    Collection<Object> findByUserAndBadge(User user, Badge badge);
}

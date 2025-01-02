package com.reddot.app.repository;

import com.reddot.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // Số người dùng mới theo ngày
    @Query("SELECT COUNT(u) FROM users u WHERE DATE(u.createdAt) = :date")
    long countNewUsersByDay(@Param("date") LocalDate date);

    // Số người dùng mới theo tuần
    @Query("SELECT COUNT(u) FROM users u WHERE YEAR(u.createdAt) = :year AND WEEK(u.createdAt) = :week")
    long countNewUsersByWeek(@Param("year") int year, @Param("week") int week);

    // Số người dùng mới theo tháng
    @Query("SELECT COUNT(u) FROM users u WHERE YEAR(u.createdAt) = :year AND MONTH(u.createdAt) = :month")
    long countNewUsersByMonth(@Param("year") int year, @Param("month") int month);

    // Số người dùng mới theo năm
    @Query("SELECT COUNT(u) FROM users u WHERE YEAR(u.createdAt) = :year")
    long countNewUsersByYear(@Param("year") int year);

}

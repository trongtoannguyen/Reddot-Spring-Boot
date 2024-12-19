package com.reddot.app.repository;

import com.reddot.app.entity.Comment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT COUNT(c) FROM comments c WHERE c.user.id = :userId")
    Long countAnswersByUserId(@NonNull Integer userId);

    @Query("SELECT c.id AS commentId, c.text AS commentText, q.id AS questionId, q.title AS questionTitle, COUNT(v.id) AS voteCount, c.createdAt AS createdAt FROM comments c " +
            "JOIN votes v ON c.id = v.comment.id " +
            "JOIN questions q ON c.question.id = q.id " +
            "WHERE c.user.id = :userId " +
            "GROUP BY c.id, q.id, c.createdAt " +
            "ORDER BY COUNT(v.id) DESC")
    List<Map<String, Object>> findTopCommentsWithQuestionsByUserId(@Param("userId") Integer userId, Pageable pageable);


    // Đếm số comment theo ngày
    @Query("SELECT COUNT(c) FROM comments c WHERE DATE(c.createdAt) = :date")
    long countCommentsByDay(@Param("date") LocalDate date);

    // Đếm số comment theo tuần
    @Query("SELECT COUNT(c) FROM comments c WHERE YEAR(c.createdAt) = :year AND WEEK(c.createdAt) = :week")
    long countCommentsByWeek(@Param("year") int year, @Param("week") int week);

    // Đếm số comment theo tháng
    @Query("SELECT COUNT(c) FROM comments c WHERE YEAR(c.createdAt) = :year AND MONTH(c.createdAt) = :month")
    long countCommentsByMonth(@Param("year") int year, @Param("month") int month);

    // Đếm số comment theo năm
    @Query("SELECT COUNT(c) FROM comments c WHERE YEAR(c.createdAt) = :year")
    long countCommentsByYear(@Param("year") int year);
}

package com.reddot.app.repository;

import com.reddot.app.entity.Question;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT COUNT(q) FROM questions q WHERE q.user.id = :userId")
    Long countQuestionsByUserId(@NonNull Integer userId); // userId kiểu Integer

    @Query("SELECT q.id AS questionId, q.title AS questionTitle, q.upvotes AS upvotes " +
            "FROM questions q " +
            "LEFT JOIN votes v ON v.question.id = q.id " +
            "WHERE q.user.id = :userId " +
            "GROUP BY q.id " +
            "ORDER BY upvotes DESC")
    List<Object[]> findTopQuestionsByVotesForUser(@Param("userId") Integer userId, Pageable pageable);

    Optional<Question> findById(Integer id);

    boolean existsByIdAndVotes_UserIdAndVotes_VoteTypeId(Integer questionId, Integer userId, int direction);
}

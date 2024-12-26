package com.reddot.app.repository;

import com.reddot.app.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(List<String> names);

    @Query("SELECT t.name AS tagName, COUNT(q) AS tagCount " +
            "FROM questions q " +
            "JOIN q.tags t " +
            "WHERE q.user.id = :userId " +
            "GROUP BY t.name " +
            "ORDER BY COUNT(q) DESC")
    List<Object[]> findTopTagsByUserId(@Param("userId") Integer userId);


    @Query(value = "SELECT t.name AS tagName, COUNT(qt.tag_id) AS usageCount " +
            "FROM tags t " +
            "JOIN question_tags qt ON t.id = qt.tag_id " +
            "GROUP BY t.name " +
            "ORDER BY usageCount DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTrendingTags();
}

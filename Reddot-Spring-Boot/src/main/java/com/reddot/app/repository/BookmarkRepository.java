package com.reddot.app.repository;

import com.reddot.app.entity.Bookmark;
import com.reddot.app.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    boolean existsByUser_IdAndQuestion_Id(Integer userId, Integer questionId);

    Integer user(@NonNull User user);

    Optional<Bookmark> findByUser_IdAndQuestion_Id(Integer userId, Integer questionId);
}

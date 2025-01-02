package com.reddot.app.repository;

import com.reddot.app.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowingRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT COUNT(f) FROM following f WHERE f.followed.id = :userId")
    Long countFollowersByUserId(Integer userId);
}

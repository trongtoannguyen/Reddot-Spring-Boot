package com.reddot.app.repository;

import com.reddot.app.entity.UserOnDelete;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface userDeleteRepository extends JpaRepository<UserOnDelete, Integer> {

    Set<UserOnDelete> findAllByCreatedAtBefore(LocalDateTime cutoffDate);

    boolean existsByUserId(@NonNull Integer userId);

    void removeByUserId(@NonNull Integer userId);

    Set<UserOnDelete> findAllByCreatedAtBeforeAndIsNoticedIsFalse(LocalDateTime cutoffDate);
}

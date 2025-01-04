package com.reddot.app.repository;

import com.reddot.app.entity.Notification;
import com.reddot.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByUserAndIsRead(User user, Boolean isRead);
}

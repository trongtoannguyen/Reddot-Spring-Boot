package com.reddot.app.service.notification;

import com.reddot.app.dto.response.NotificationDTO;
import com.reddot.app.entity.Notification;
import com.reddot.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO: implement sorting and filtering
@Component
public interface NotificationService {

    /**
     * Get all unread notifications for a user.
     *
     * @param user the user whose unread notifications are to be fetched
     * @return a list of NotificationDTO objects representing unread notifications
     */
    // todo: https://api.stackexchange.com/docs/questions-by-ids
    List<NotificationDTO> getAllUnreadNotificationByUser(User user);

    /**
     * Mark notifications as read for a user.
     *
     * @param user the user whose notifications are to be marked as read
     */
    void markNotificationsAsRead(User user);
    void sendUnreadNotifications(List<Notification> notifications, User user);


}
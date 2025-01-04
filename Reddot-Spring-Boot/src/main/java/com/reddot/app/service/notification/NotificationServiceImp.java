package com.reddot.app.service.notification;

import com.reddot.app.dto.response.NotificationDTO;
import com.reddot.app.entity.Notification;
import com.reddot.app.entity.User;
import com.reddot.app.repository.NotificationRepository;
import com.reddot.app.service.email.MailSenderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MailSenderManager mailSenderManager;

    @Override
    public List<NotificationDTO> getAllUnreadNotificationByUser(User user) {
        List<Notification> unreadNotifications = notificationRepository.findAllByUserAndIsRead(user, false);

        List<NotificationDTO> notificationDTOS = unreadNotifications.stream()
                .map(notification -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setId(notification.getId());
                    dto.setRead(notification.isRead());
                    dto.setMessage(notification.getMessage());
                    dto.setCreatedAt(notification.getCreatedAt());
                    dto.setUserId(notification.getUser().getId());
                    dto.setEmail(notification.getUser().getEmail());
                    dto.setUsername(notification.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());

        return notificationDTOS;
    }

    @Override
    public void markNotificationsAsRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findAllByUserAndIsRead(user, false);

        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }

        log.info("Marked {} notifications as read for user {}", unreadNotifications.size(), user.getUsername());
    }

    @Override
    public void sendUnreadNotifications(List<Notification> notifications, User user) {
        try {
            String subject = "Reddot Unread Notifications";

            StringBuilder body = new StringBuilder();
            body.append("<h3>Unread Notifications:</h3>");

            if (notifications != null && !notifications.isEmpty()) {
                body.append("<ul>");

                int counter = 1;
                for (Notification notification : notifications) {
                    body.append("<li style=\"font-size: 16px;\">")
                            .append(counter)
                            .append(". ")
                            .append(notification.getMessage())
                            .append("</li>");
                    counter++;
                }
                body.append("</ul>");
            } else {
                body.append("<p>No unread notifications.</p>");
            }

            body.append("<br><br>")
                    .append("<img src='https://www.reddotcorp.com/uploads/1/2/7/5/12752286/reddotlogo.png' alt='Welcome' width='300' style=\"border-radius: 8px;\"/>")
                    .append("<br><br>")
                    .append("<p style=\"font-size: 14px; color: #888;\">If you didn't request this, please ignore this email.</p>")
                    .append("<p style=\"font-size: 14px;\">Best regards,<br>The Reddot Team</p>")
                    .append("</body></html>");

            mailSenderManager.sendEmail(user.getEmail(), subject, body.toString());

        } catch (Exception e) {
            throw new RuntimeException("Failed to send notifications", e);
        }
    }

}

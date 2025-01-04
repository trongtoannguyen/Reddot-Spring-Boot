package com.reddot.app.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Integer id;
    private LocalDateTime createdAt;
    private String message;
    private boolean isRead;
    private Integer userId;
    private String username;
    private String email;
}

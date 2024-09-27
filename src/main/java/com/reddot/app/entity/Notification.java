package com.reddot.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;


@Entity(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Notification extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "delete_request")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class UserOnDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_noticed")
    private Boolean isNoticed = false;

    public UserOnDelete(@NonNull Integer userId) {
        this.userId = userId;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

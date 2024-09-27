package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "user_badges")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@IdClass(UserBadge.UserBadgeId.class)
public class UserBadge {

    // TODO: implement cascade type properly
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NonNull
    private User user;


    // TODO: implement cascade type properly
    @Id
    @ManyToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "id")
    @NonNull
    private Badge badge;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.setAchievedAt(now);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserBadgeId implements Serializable {
        private int user;
        private int badge;

    }
}

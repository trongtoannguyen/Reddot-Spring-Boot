package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "user_badges")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@IdClass(UserBadge.UserBadgeId.class)
public class UserBadge implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NonNull
    private User user;


    @Id
    @ManyToOne(fetch = FetchType.LAZY)
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
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserBadgeId implements Serializable {
        private Integer user;
        private Integer badge;

    }
}

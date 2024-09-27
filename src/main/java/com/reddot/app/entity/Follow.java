package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "following")
@IdClass(Follow.FollowId.class)
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class Follow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    @NonNull
    private User follower;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", referencedColumnName = "id")
    @NonNull
    private User followed;

    @Column(name = "subscribed_at", columnDefinition = "DATE")
    private LocalDate subscribedAt;

    @PrePersist
    public void prePersist() {
        LocalDate now = LocalDate.now();
        this.setSubscribedAt(now);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Setter
    @Getter
    public static class FollowId implements Serializable {
        private Integer follower;
        private Integer followed;
    }
}
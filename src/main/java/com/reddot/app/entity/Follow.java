package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "following")
@IdClass(Follow.FollowId.class)
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Follow {

    @Id
    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    @NonNull
    private User follower;

    @Id
    @ManyToOne
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
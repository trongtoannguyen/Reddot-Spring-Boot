package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "user_oauth")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserOAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "refresh_token")
    private String refreshToken;

    // do not save in database, just manage in client local storage
    @Transient
    private String accessToken;

    @Column(name = "expiration_before")
    private LocalDateTime expirationBefore;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "provider")
    private String provider;

    @Column(name = "oauth_id", unique = true)
    private String oauthId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User user;

    @PrePersist
    @PreUpdate
    public void onPreAction() {
        LocalDateTime now = LocalDateTime.now();
        this.setIssuedAt(now);
    }
}

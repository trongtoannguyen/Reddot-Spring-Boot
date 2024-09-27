package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "jwt_tokens")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String value;

    @Column(name = "expiration_before", columnDefinition = "TIMESTAMP")
    private Timestamp expirationBefore;

    @Column(name = "issued_at", columnDefinition = "TIMESTAMP")
    private Timestamp issuedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_name")
    private String deviceName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User user;

    @PrePersist
    @PreUpdate
    public void onPreAction() {
        LocalDateTime now = LocalDateTime.now();
        this.setIssuedAt(Timestamp.valueOf(now));
    }
}

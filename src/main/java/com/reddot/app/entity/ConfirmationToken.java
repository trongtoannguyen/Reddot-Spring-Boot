package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "confirm_tokens")
@Setter
@Getter
@NoArgsConstructor
public class ConfirmationToken extends BaseEntity {
    private String token;
    @NonNull
    @Column(name = "owner_id")
    private Integer ownerId;
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    public ConfirmationToken(@NonNull Integer ownerId) {
        this.token = UUID.randomUUID().toString();
        this.ownerId = ownerId;
    }
}

package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
    @Column(name = "valid_before")
    private LocalDateTime validBefore;

    public ConfirmationToken(@NonNull Integer ownerId) {
        this.token = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        this.validBefore = LocalDateTime.now().plusHours(24);
    }
}

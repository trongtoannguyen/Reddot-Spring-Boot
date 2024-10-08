package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for saving recovery tokens when a user requests a password reset.
 */
@Entity(name = "recovery_tokens")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class RecoveryToken extends BaseEntity{

    @NonNull
    private String token;
    @NonNull
    @Column(name = "owner_id")
    private Integer ownerId;
    private boolean used;
    @Column(name = "valid_before")
    private LocalDateTime validBefore;

    public RecoveryToken(@NonNull Integer ownerId) {
        this.token = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        this.used = false;
        this.validBefore = LocalDateTime.now().plusMinutes(30);
    }

    public RecoveryToken(@NonNull Integer ownerId, @NonNull String token, LocalDateTime validBefore){
        this.ownerId = ownerId;
        this.token = token;
        this.validBefore = validBefore;
        this.used = false;
    }

    @Override
    public String toString() {
        return "RecoveryToken{" +
                "token='" + token + '\'' +
                ", ownerId=" + ownerId +
                ", used=" + used +
                ", validBefore=" + validBefore +
                '}';
    }
}

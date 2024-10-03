package com.reddot.app.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

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
    private String email;
    private String token;
    private boolean used;
    private LocalDateTime validBefore;

    public RecoveryToken(@NonNull String email, String token, LocalDateTime validBefore){
        this.email = email;
        this.token = token;
        this.validBefore = validBefore;
        this.used = false;
    }

    @Override
    public String toString(){
        return "RecoveryToken{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", used=" + used +
                '}';
    }
}

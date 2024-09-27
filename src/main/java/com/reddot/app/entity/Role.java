package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.ROLENAME;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "role_name", unique = true)
    private ROLENAME name;

    public Role(@NonNull ROLENAME name) {
        Assert.hasText(String.valueOf(name), "Role textual representation is required");
        this.name = name;
    }
}

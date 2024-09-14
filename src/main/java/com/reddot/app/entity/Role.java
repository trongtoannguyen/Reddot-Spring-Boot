package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.ROLENAME;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.Assert;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(unique = true)
    private ROLENAME name;

    public Role(@NonNull ROLENAME name) {
        Assert.hasText(String.valueOf(name), "Role textual representation is required");
        this.name = name;
        this.setCreatedBy("System");
    }
}

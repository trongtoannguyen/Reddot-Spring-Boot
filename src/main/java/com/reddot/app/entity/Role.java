package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.ROLENAME;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    @NonNull
    private ROLENAME name;

    public Role(@NonNull ROLENAME name) {
        this.name = name;
        this.setCreatedBy("System");
    }
}

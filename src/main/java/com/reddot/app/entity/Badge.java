package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "badges")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Badge extends BaseEntity {

    @NonNull
    @Size(min = 3, max = 50)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    private String description;

    @OneToMany(mappedBy = "badge")
    private Set<UserBadge> userBadges = new HashSet<>();
}

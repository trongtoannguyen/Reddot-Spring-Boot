package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;

@Entity(name = "badges")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Badge extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NonNull
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @NonNull
    private String description;
}

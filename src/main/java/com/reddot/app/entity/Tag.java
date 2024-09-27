package com.reddot.app.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * The {@code Tag} class represents a tag entity.
 * <p>
 * Supported types are:
 * <ul>
 *     <li>{@code tagged} (int) - the number of times a tag has been used
 * </ul>
 * <p>
 */
@Entity(name = "tags")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseEntity {

    @NonNull
    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 100)
    private String description;

    private int tagged;
}

package com.reddot.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@EqualsAndHashCode()
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(min = 2, max = 21)
    private String name;

    @Size(min = 3, max = 100)
    private String description;

    private int tagged;

}

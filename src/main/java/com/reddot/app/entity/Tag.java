package com.reddot.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private int tagged;

    public void incrementTagged() {
        tagged++;
    }
}

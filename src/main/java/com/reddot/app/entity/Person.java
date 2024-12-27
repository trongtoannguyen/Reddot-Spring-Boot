package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;
import java.time.LocalDate;

/**
 * The Person class is an entity model object. It represents public information about the user.
 */
@Entity(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Person extends BaseEntity {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @NonNull
    @Column(name = "display_name")
    @Size(min = 3, max = 100)
    private String displayName;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "dob", columnDefinition = "DATE")
    private LocalDate dob;

    private String location;

    @Column(name = "website_url")
    private String websiteUrl;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

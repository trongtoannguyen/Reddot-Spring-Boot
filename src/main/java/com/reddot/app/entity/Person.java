package com.reddot.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Person extends BaseEntity {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Column(name = "display_name")
    @Size(min = 3, max = 100)
    private String displayName;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "dob", columnDefinition = "DATE")
    private LocalDate dob;

    @Column(name = "last_access", columnDefinition = "DATE")
    private LocalDate lastAccess;

    private String location;

    @Column(name = "website_url")
    private String websiteUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

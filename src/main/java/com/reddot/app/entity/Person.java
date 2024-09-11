package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.GENDER;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity {

    @Column(name = "display_name")
    @Size(min = 3, max = 100)
    private String displayName;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    private GENDER gender;

    @Column(name = "phone", length = 50)
    private String phone;

    private String address;

    private String bio;

    @Lob
    @Column(name = "profile_banner")
    private byte[] profileBanner;
}

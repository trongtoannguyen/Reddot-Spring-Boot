package com.reddot.app.dto;

import com.reddot.app.entity.Person;
import com.reddot.app.entity.User;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    @Size(min = 3, max = 100)
    private String displayName;
    private String aboutMe;
    private LocalDate dob;
    private String location;
    private String websiteUrl;

    public void builder(User user, Person person) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatarUrl();
        this.displayName = person.getDisplayName();
        this.aboutMe = person.getAboutMe();
        this.dob = person.getDob();
        this.location = person.getLocation();
        this.websiteUrl = person.getWebsiteUrl();
    }
}

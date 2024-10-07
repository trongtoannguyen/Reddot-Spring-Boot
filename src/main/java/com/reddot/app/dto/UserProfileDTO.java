package com.reddot.app.dto;

import com.reddot.app.entity.Person;
import com.reddot.app.entity.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Integer id;
    @NonNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,30}$")
    private String username;
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
        this.avatar = user.getAvatarUrl();
        this.displayName = person.getDisplayName();
        this.aboutMe = person.getAboutMe();
        this.dob = person.getDob();
        this.location = person.getLocation();
        this.websiteUrl = person.getWebsiteUrl();
    }
}

package com.reddot.app.dto.request;

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
public class ProfileUpdateRequest {
    private String avatar;
    @Size(min = 3, max = 100)
    private String displayName;
    private String aboutMe;
    private LocalDate dob;
    private String location;
    private String websiteUrl;

    // helper update profile method
    public void updateProfile(User user, Person person) {
        user.setAvatarUrl(avatar);
        person.setDisplayName(displayName);
        person.setAboutMe(aboutMe);
        person.setDob(dob);
        person.setLocation(location);
        person.setWebsiteUrl(websiteUrl);
        user.setPerson(person);
    }
}

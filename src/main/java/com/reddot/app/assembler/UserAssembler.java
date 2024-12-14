package com.reddot.app.assembler;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.entity.Person;
import com.reddot.app.entity.User;

/**
 * Assembler/Mapper: Assembling a Data Transfer Object from Domain Objects
 */
public class UserAssembler {
    /**
     * Assemble UserProfileDTO from User and Person
     * @return UserProfileDTO
     */
    public static UserProfileDTO toUserProfileDTO(User user, Person person) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatarUrl())
                .displayName(person.getDisplayName())
                .aboutMe(person.getAboutMe())
                .dob(person.getDob())
                .location(person.getLocation())
                .websiteUrl(person.getWebsiteUrl())
                .build();
    }
}

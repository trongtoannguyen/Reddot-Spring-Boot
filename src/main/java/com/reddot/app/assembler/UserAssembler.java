package com.reddot.app.assembler;

import com.reddot.app.dto.response.ShallowUserDTO;
import com.reddot.app.dto.response.UserProfileDTO;
import com.reddot.app.entity.Person;
import com.reddot.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Assembler/Mapper: Assembling a Data Transfer Object from Domain Objects
 */
@Mapper(componentModel = "spring")
@Component
public interface UserAssembler {

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

    // TODO: IMPLEMENT CountBadge() map list/set
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "displayName", source = "person.displayName")
    @Mapping(target = "profileImage", source = "avatarUrl")
    @Mapping(target = "websiteLink", source = "person.websiteUrl")
    @Mapping(target = "badgeCounts", ignore = true)
    ShallowUserDTO userToShallowUserDTO(User u);
}

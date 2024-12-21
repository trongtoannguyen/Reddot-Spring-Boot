package com.reddot.app.dto.response;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

// TODO: composition ShallowUserDTO
@Data
public class UserProfileDTO {
    private Integer userId;
    private String username;
    private String email;
    private String avatarLink;
    @Size(min = 3, max = 100)
    private String displayName;
    private String aboutMe;
    private LocalDate dob;
    private String location;
    private String websiteUrl;
}

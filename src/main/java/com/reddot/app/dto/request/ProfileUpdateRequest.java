package com.reddot.app.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    private Integer id;
    private String avatar;
    @Size(min = 3, max = 100)
    private String displayName;
    private String aboutMe;
    private LocalDate dob;
    private String location;
    private String websiteUrl;
}

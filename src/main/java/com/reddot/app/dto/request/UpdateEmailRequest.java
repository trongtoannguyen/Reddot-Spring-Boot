package com.reddot.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateEmailRequest {
    @NonNull
    private String token;

    @NonNull
    @Email
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-zA-Z]{2,10}$",
            message = "Invalid email format")
    private String email;
}

package com.reddot.app.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NonNull
    private String token;

    @NonNull
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",
            message = "Password must contain minimum 8 characters, at least one uppercase letter," +
                    "one lowercase letter, one number and one special character")
    private String password;
}

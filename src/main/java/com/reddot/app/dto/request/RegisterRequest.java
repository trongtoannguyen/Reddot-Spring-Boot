package com.reddot.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NonNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,30}$")
    private String username;
    @NonNull
    @Email
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-zA-Z]{2,10}$",
            message = "Invalid email format")
    private String email;
    @NonNull
    @Size(min = 8,max = 30)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",
            message = "Password must contain minimum 8 characters, at least one uppercase letter," +
                    "one lowercase letter, one number and one special character")
    private String password;

    private Set<String> roles;
}
package com.reddot.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @NotBlank(message = "Email is required")
    @Email
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-zA-Z]{2,10}$",
            message = "Invalid email format")
    private String email;
}

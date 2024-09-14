package com.reddot.app.authentication.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}



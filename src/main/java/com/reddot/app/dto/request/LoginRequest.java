package com.reddot.app.dto.request;

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



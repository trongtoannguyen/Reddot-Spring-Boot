package com.reddot.app.service.user;

import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.authentication.dto.UpdatePasswordRequest;
import com.reddot.app.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceManager extends UserDetailsService {

    void createNewUser(RegisterRequest request);

    void sendPasswordResetEmail(String email);

    boolean userExists(String username);

    User confirmNewUser(String token);

    void resetPassword(UpdatePasswordRequest request);
}

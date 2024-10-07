package com.reddot.app.service.user;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.dto.request.UpdateEmailRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceManager extends UserDetailsService {

    void createNewUser(RegisterRequest request);

    UserProfileDTO confirmNewUser(String token);

    UserProfileDTO getUserProfile(String username);

    UserProfileDTO updateProfile(String username, @Valid ProfileUpdateRequest request);

    void sendPasswordResetEmail(String email);

    void resetPassword(UpdatePasswordRequest request);

    void sendUpdateEmail(String newEmail);

    void confirmNewEmail(UpdateEmailRequest request);
}

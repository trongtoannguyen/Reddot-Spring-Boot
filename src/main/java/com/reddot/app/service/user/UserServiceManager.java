package com.reddot.app.service.user;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public interface UserServiceManager extends UserDetailsService {

    void createNewUser(RegisterRequest request);

    UserProfileDTO confirmNewUser(String token);

    UserProfileDTO getUserProfile(Integer userId);

    UserProfileDTO getUserProfile(String username);

    UserProfileDTO updateProfile(Integer userId, @Valid ProfileUpdateRequest request);

    void sendPasswordResetEmail(String email) throws ResourceNotFoundException;

    void resetPassword(UpdatePasswordRequest request);

    void updateEmail(Integer userId, String newEmail) throws ResourceNotFoundException;

    void confirmNewEmail(@NonNull String token) throws ResourceNotFoundException;

    void resendEmailConfirm(Integer userId) throws ResourceNotFoundException;
}

package com.reddot.app.service.user;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Service for managing user operations.
 */
@Component
public interface UserServiceManager extends UserDetailsService {

    void userCreate(RegisterRequest request);

    User userConfirm(String token);

    void userDeleteRequest(Integer userId) throws ResourceNotFoundException;

    void userOnLoginUpdate(@NonNull String email);

    UserProfileDTO profileGetById(Integer userId);

    UserProfileDTO profileGetByUsername(String username);

    UserProfileDTO profileUpdate(Integer userId, @Valid ProfileUpdateRequest request);

    void pwForgot(String email) throws ResourceNotFoundException;

    void pwReset(UpdatePasswordRequest request);

    void emailUpdate(Integer userId, String newEmail) throws ResourceNotFoundException;

    void emailConfirm(@NonNull String token) throws ResourceNotFoundException;

    void emailConfirmResend(Integer userId) throws ResourceNotFoundException;
}

package com.reddot.app.controller;

import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.service.user.UserServiceManager;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecoveryController {

    private final UserServiceManager userServiceManager;

    public RecoveryController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    // POST request to send reset password with email param
    @PostMapping("/reset-password")
    public ResponseEntity<ServiceResponse<String>> sendResetPassword(@RequestParam @NonNull String email) {
        try {
            // Send email with reset password link
            userServiceManager.sendPasswordResetEmail(email);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), " Reset password email sent. Check your email box or spam folder."), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // POST request to reset password with token param
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ServiceResponse<String>> confirmPassword(@Valid @RequestBody UpdatePasswordRequest request) {
        try {
            // Confirm password reset
            userServiceManager.resetPassword(request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Password reset successfully"), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

package com.reddot.app.controller;

import com.reddot.app.dto.request.EmailUpdateRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class RecoveryController {

    private final UserServiceManager userServiceManager;

    public RecoveryController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    // POST request to send reset password request to email
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

    // Email Update by user id
    @PutMapping("/email/edit/{id}")
    public ResponseEntity<ServiceResponse<Void>> updateEmail(@PathVariable Integer id, @Valid @RequestBody EmailUpdateRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = (User) userServiceManager.loadUserByUsername(currentPrincipalName);
            if (!user.getId().equals(id)) {
                throw new ResourceNotFoundException("Unable to update email");
            }
            userServiceManager.updateEmail(user.getId(), request.getNewEmail());
            ServiceResponse<Void> response = new ServiceResponse<>(HttpStatus.OK.value(), "Check your email or spam to confirm the new email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    // confirm mail update
    @GetMapping("/email/confirm")
    public ResponseEntity<ServiceResponse<Void>> confirmEmail(@RequestParam String token) {
        try {
            userServiceManager.confirmNewEmail(token);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Email updated successfully"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    // resend email confirm
    @GetMapping("/email/resend-confirm")
    public ResponseEntity<ServiceResponse<Void>> resendEmailConfirm() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = (User) userServiceManager.loadUserByUsername(currentPrincipalName);
            userServiceManager.resendEmailConfirm(user.getId());
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Check your email or spam to confirm the new email"), HttpStatus.OK);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}

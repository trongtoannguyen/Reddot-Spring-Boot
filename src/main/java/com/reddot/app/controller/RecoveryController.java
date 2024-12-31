package com.reddot.app.controller;

import com.reddot.app.dto.request.EmailRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import jakarta.validation.Valid;
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

    /**
     * Do not require authentication
     * Function describe: When user click on forgot password, prompt to enter target email.
     * App send reset password email with code (recovery token).
     * User click on the link in email and enter new password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ServiceResponse<String>> sendResetPassword(@Valid @RequestBody EmailRequest request) {
        try {
            // Send email with reset password link
            userServiceManager.pwForgot(request.getEmail());
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), " Reset password email sent. Check your email box or spam folder."), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email not registered");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
      GET request method is not supported by Server
      instead this should be implemented by Client side
     */

    /**
     * Do not require authentication
     * Function describe: User post new password with code (recovery token) to reset password.
     * App then check if the token is valid and update the password.
     */
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ServiceResponse<String>> confirmPassword(@Valid @RequestBody UpdatePasswordRequest request) {
        try {
            // Confirm password reset
            userServiceManager.pwReset(request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Password reset successfully"), HttpStatus.OK);
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Email Update by user id
    @PutMapping("/email/edit/{id}")
    public ResponseEntity<ServiceResponse<Void>> updateEmail(@PathVariable Integer id, @Valid @RequestBody EmailRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) userServiceManager.loadUserByUsername(authentication.getName());
            if (!user.getId().equals(id)) {
                throw new ResourceNotFoundException("Unable to update email");
            }
            userServiceManager.emailUpdate(user.getId(), request.getEmail());
            ServiceResponse<Void> response = new ServiceResponse<>(HttpStatus.OK.value(), "Check your email or spam to confirm the new email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // confirm mail update
    @GetMapping("/email/confirm")
    public ResponseEntity<ServiceResponse<Void>> confirmEmail(@RequestParam String token) {
        try {
            userServiceManager.emailConfirm(token);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Email updated successfully"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    // resend email confirm
    // FIXME: UPDATE LOAD PRINCIPAL WITH AUTHENTICATION
    @GetMapping("/email/resend-confirm")
    public ResponseEntity<ServiceResponse<Void>> resendEmailConfirm(@Valid @RequestBody EmailRequest request) {
        try {
            User user = userServiceManager.loadUserByEmail(request.getEmail());
            userServiceManager.emailConfirmResend(user.getId());
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Check your email or spam to confirm the new email"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // return 200 OK even if email not found
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Check your email or spam to confirm the new email"), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

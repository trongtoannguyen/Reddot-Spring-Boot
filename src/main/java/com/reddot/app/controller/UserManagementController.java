package com.reddot.app.controller;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserManagementController {

    private final UserServiceManager userServiceManager;

    public UserManagementController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> getUserProfile(@PathVariable String username) {
        try {
            UserProfileDTO profileDTO = userServiceManager.getUserProfile(username);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Retrieve profile successfully", profileDTO), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> updateProfile(@PathVariable String username,
                                                                         @Valid @RequestBody ProfileUpdateRequest request) {
        try {
            // FIXME: RESOLVE LAZY INITIALIZATION EXCEPTION
            // get the current context username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            UserDetails userDetails = userServiceManager.loadUserByUsername(username);

            if (!currentPrincipalName.equals(userDetails.getUsername())) {
                throw new RuntimeException("You are not authorized to update this profile");
            }
            UserProfileDTO updatedProfile = userServiceManager.updateProfile(username, request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Profile updated successfully", updatedProfile), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

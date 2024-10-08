package com.reddot.app.controller;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Users can update their profile with Bearer token contains their username(sub)
     *
     * @param request ProfileUpdateRequest
     * @return ResponseEntity<ServiceResponse < UserProfileDTO>>
     */
    @PutMapping
    public ResponseEntity<ServiceResponse<UserProfileDTO>> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        try {
            // FIXME: RESOLVE LAZY INITIALIZATION EXCEPTION
            // get the current context username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = (User) userServiceManager.loadUserByUsername(currentPrincipalName);
            UserProfileDTO updatedProfile = userServiceManager.updateProfile(user.getId(), request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Profile updated successfully", updatedProfile), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> updateProfile(@PathVariable Integer id, @Valid @RequestBody ProfileUpdateRequest request, HttpServletRequest servletRequest) {
        try {
            Integer userId = (Integer) servletRequest.getAttribute("userId");
            // FIXME: RESOLVE LAZY INITIALIZATION EXCEPTION
            // get the current user id
            if (!userId.equals(id)) {
                throw new ResourceNotFoundException("Unable to update profile");
            }
            UserProfileDTO updatedProfile = userServiceManager.updateProfile(id, request);
            ServiceResponse<UserProfileDTO> response = new ServiceResponse<>(HttpStatus.OK.value(), "Profile updated successfully", updatedProfile);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

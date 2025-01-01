package com.reddot.app.controller;

import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.dto.response.UserProfileDTO;
import com.reddot.app.entity.User;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserManagementController {

    private final UserServiceManager userServiceManager;

    public UserManagementController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    @Operation(summary = "Get user profile by username",
            description = "Get user profile by username")
    @GetMapping
    public ResponseEntity<ServiceResponse<UserProfileDTO>> getUserProfile(@RequestParam String username) {
        try {
            UserProfileDTO profileDTO = userServiceManager.profileGetByUsername(username);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Retrieve profile successfully", profileDTO), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Get user profile by id",
            description = "Get user profile by id")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> getUserProfileById(@PathVariable Integer id) {
        try {
            UserProfileDTO profileDTO = userServiceManager.profileGetById(id);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Retrieve profile successfully", profileDTO), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> updateProfile(@PathVariable Integer id,
                                                                         @Valid @RequestBody ProfileUpdateRequest request) {
        try {
            // FIXME: RESOLVE LAZY INITIALIZATION EXCEPTION
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) userServiceManager.loadUserByUsername(authentication.getName());
            if (!user.getId().equals(id)) {
                throw new BadRequestException("Unable to update profile");
            }
            UserProfileDTO updatedProfile = userServiceManager.profileUpdate(user.getId(), request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Profile updated successfully", updatedProfile), HttpStatus.OK);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ServiceResponse<Void>> userDelete(@RequestParam Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) userServiceManager.loadUserByUsername(authentication.getName());
            if (!user.getId().equals(id)) {
                throw new BadRequestException("Unable to delete account");
            }
            userServiceManager.userDeleteRequest(user.getId());
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Account is in delete queue now, you have 15 days to cancel the request"), HttpStatus.OK);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
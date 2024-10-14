package com.reddot.app.controller;

import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
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
@RequestMapping("/users")
public class UserManagementController {

    private final UserServiceManager userServiceManager;

    public UserManagementController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    @GetMapping
    public ResponseEntity<ServiceResponse<UserProfileDTO>> getUserProfile(@RequestParam String username) {
        try {
            UserProfileDTO profileDTO = userServiceManager.profileGetBy(username);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Retrieve profile successfully", profileDTO), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> getUserProfileById(@PathVariable Integer id) {
        try {
            UserProfileDTO profileDTO = userServiceManager.profileGetBy(id);
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
            // get the current context username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = (User) userServiceManager.loadUserByUsername(currentPrincipalName);
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
            String currentPrincipalName = authentication.getName();
            User user = (User) userServiceManager.loadUserByUsername(currentPrincipalName);
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

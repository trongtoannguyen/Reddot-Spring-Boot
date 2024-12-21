package com.reddot.app.controller;

import com.reddot.app.dto.request.LoginRequest;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.dto.response.ServiceResponse;
import com.reddot.app.dto.response.UserProfileDTO;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.service.user.UserServiceManager;
import com.reddot.app.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// FIXME: implement validation
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtUtil jwtUtil;

    private final UserServiceManager userServiceManager;

    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtUtil jwtUtil, UserServiceManager userServiceManager, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userServiceManager = userServiceManager;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String createAuthenticationToken(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Authenticate user by creating new empty context and to avoid race conditions across multiple threads.
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            final User user = (User) userServiceManager.loadUserByEmail(request.getEmail());
            userServiceManager.userOnLoginUpdate(request.getEmail());
            return jwtUtil.generateToken(user);
        } catch (DisabledException e) {
            throw new ResourceNotFoundException("Account is not confirmed, please check your email to confirm account registration");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse<RegisterRequest>> register(@Valid @RequestBody RegisterRequest request) { // @Valid: validate the request body and throw Bad Request if invalid
        try {
            userServiceManager.userCreate(request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.CREATED.value(), "Check email box to confirm account registration or you may want to check spam folder", request), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Confirm account registration
     *
     * @param token token from email link to confirm account registration
     * @return due to security reasons, return user profile DTO to hide sensitive information
     */
    @GetMapping("/confirm-account")
    public ResponseEntity<ServiceResponse<UserProfileDTO>> confirm(@RequestParam("token") String token) {
        try {
            User user = userServiceManager.userConfirm(token);
            UserProfileDTO dto = userServiceManager.profileGetById(user.getId());
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.OK.value(), "Congratulations! Your account has been confirmed", dto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
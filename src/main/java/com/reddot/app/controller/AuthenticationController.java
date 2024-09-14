package com.reddot.app.controller;

import com.reddot.app.authentication.dto.LoginRequest;
import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.dto.ServiceResponse;
import com.reddot.app.entity.User;
import com.reddot.app.service.user.UserServiceManager;
import com.reddot.app.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// FIXME: implement validation
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final UserServiceManager manager;

    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserServiceManager manager, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String createAuthenticationToken(@Valid @RequestBody LoginRequest request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // Authenticate user by creating new empty context and to avoid race conditions across multiple threads.
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse<User>> register(@Valid @RequestBody RegisterRequest request) { // @Valid: validate the request body and throw Bad Request if invalid
        try {
            User user = manager.createNewUser(request);
            return new ResponseEntity<>(new ServiceResponse<>(HttpStatus.CREATED.value(), "User created", user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
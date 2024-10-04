package com.reddot.app.service.user;

import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.entity.User;
import com.reddot.app.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceManager extends UserDetailsService {

    void createNewUser(RegisterRequest request);

    void sendPasswordResetEmail(String email) throws ResourceNotFoundException;

    boolean userExists(String username);

    User confirmNewUser(String token);

}

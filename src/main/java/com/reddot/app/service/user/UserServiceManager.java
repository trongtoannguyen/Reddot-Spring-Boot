package com.reddot.app.service.user;

import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.entity.User;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserServiceManager extends UserDetailsManager {
    User createNewUser(RegisterRequest request);
}

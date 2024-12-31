package com.reddot.app.service.system;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SystemAuthentication {
    public static boolean isLoggedIn(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
               && !(authentication instanceof AnonymousAuthenticationToken);
    }
}

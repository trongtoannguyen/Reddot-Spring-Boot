package com.reddot.app.config.security.jwt;

import com.reddot.app.service.user.UserServiceManager;
import com.reddot.app.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// This filter will intercept requests to extract and validate the JWT token.
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserServiceManager userServiceManager;

    public JwtTokenFilter(JwtUtil jwtUtil, @Lazy UserServiceManager userServiceManager) {
        this.jwtUtil = jwtUtil;
        this.userServiceManager = userServiceManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jws = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jws = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jws);
            } catch (JwtException e) {
                logger.error("Invalid JWT token");
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userServiceManager.loadUserByUsername(email);

            if (jwtUtil.validateToken(jws, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

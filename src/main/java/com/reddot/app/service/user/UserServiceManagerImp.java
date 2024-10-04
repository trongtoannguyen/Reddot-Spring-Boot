package com.reddot.app.service.user;

import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.authentication.dto.UpdatePasswordRequest;
import com.reddot.app.entity.ConfirmationToken;
import com.reddot.app.entity.RecoveryToken;
import com.reddot.app.entity.Role;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.ConfirmationTokenRepository;
import com.reddot.app.repository.RecoveryTokenRepository;
import com.reddot.app.repository.RoleRepository;
import com.reddot.app.repository.UserRepository;
import com.reddot.app.service.email.MailSenderManager;
import com.reddot.app.util.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
public class UserServiceManagerImp implements UserServiceManager {

    private final MailSenderManager mailSenderManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final RecoveryTokenRepository recoveryTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final String fullUrl;

    public UserServiceManagerImp(@Value("${server.address}") String appDomain,
                                 @Value("${server.port}") String appPort,
                                 @Value("${server.servlet.context-path}") String appPath,
                                 MailSenderManager sender, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, RecoveryTokenRepository recoveryTokenRepository, ConfirmationTokenRepository confirmationTokenRepository) {
        this.mailSenderManager = sender;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.recoveryTokenRepository = recoveryTokenRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.fullUrl = "http://" + appDomain + ":" + appPort + appPath;
    }

    @Override
    public boolean userExists(String username) {
        Assert.notNull(username, "Username is null");
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<GrantedAuthority> dbAuths = new ArrayList<>(loadUserAuthorities(username));
        return createUserDetails(user, dbAuths);
    }

    // TODO: Implement this method
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        Assert.notNull(username, "Username is null");
        List<GrantedAuthority> dbAuths = new ArrayList<>();
        dbAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return dbAuths;
    }

    // Helper method
    protected UserDetails createUserDetails(User userFromDb, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromDb.getUsername();
        return new org.springframework.security.core.userdetails.User(returnUsername, userFromDb.getPassword(), userFromDb.isEnabled(), userFromDb.isAccountNonExpired(), userFromDb.isCredentialsNonExpired(), userFromDb.isAccountNonLocked(), combinedAuthorities);
    }

    @Override
    public void createNewUser(RegisterRequest request) {
        try {
            if (userExists(request.getUsername())) {
                throw new Exception("USER_ALREADY_EXISTS");
            }
            List<String> errorMessages = validateUser(request);
            if (!errorMessages.isEmpty()) {
                log.error(String.valueOf(errorMessages));
                throw new Exception(String.valueOf(errorMessages));
            }
            User user = new User(request.getUsername(), request.getEmail(),
                    encoder.encode(request.getPassword())
            );
            Set<Role> roles = getRolesByString(request.getRoles());
            user.setRoles(roles);
            userRepository.save(user);

            // Send confirmation email
            ConfirmationToken token = new ConfirmationToken(user.getEmail());
            String subject = "Reddot account confirmation";
            String body = "Hi there,\n\n" +
                    "To confirm your account, click the link below:\n"
                    + fullUrl + "/auth/confirm-account?token=" + token.getToken();
            mailSenderManager.sendEmail(user.getEmail(), subject, body);

            // Save confirmation token
            confirmationTokenRepository.save(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User confirmNewUser(String token) {
        try {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("TOKEN_NOT_FOUND"));
            if (confirmationToken.getConfirmedAt() != null) {
                throw new Exception("EMAIL_ALREADY_CONFIRMED");
            }
            User user = userRepository.findByEmail(confirmationToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
            user.setEnabled(true);
            userRepository.save(user);
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            return user;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(UpdatePasswordRequest request) {
        try {
            RecoveryToken recoveryToken = recoveryTokenRepository.findByToken(request.getToken()).orElseThrow(() -> new ResourceNotFoundException("TOKEN_NOT_FOUND"));
            if (!Validator.isPasswordValid(request.getPassword())) {
                throw new Exception("INVALID_PASSWORD_FORMAT");
            }
            if (recoveryToken.isUsed()) {
                throw new Exception("TOKEN_ALREADY_USED");
            }
            if (recoveryToken.getValidBefore().isBefore(LocalDateTime.now())) {
                throw new Exception("TOKEN_EXPIRED");
            }
            User user = userRepository.findByEmail(recoveryToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
            user.setPassword(encoder.encode(request.getPassword()));
            userRepository.save(user);
            recoveryToken.setUsed(true);
            recoveryTokenRepository.save(recoveryToken);

            // send email
            String subject = "Reddot password reset successful";
            String body = """
                    Hi there,

                    Your password has been reset successfully.
                    """;
            mailSenderManager.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        try {
            // check if email exists in the system
            if (!userRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException("EMAIL_NOT_FOUND");
            }

            // send password reset email
            RecoveryToken recoveryToken = new RecoveryToken();
            recoveryToken.setEmail(email);
            recoveryToken.setToken(UUID.randomUUID().toString());
            LocalDateTime validBefore = LocalDateTime.now().plusHours(24);
            recoveryToken.setValidBefore(validBefore);

            // send password reset email
            String subject = "Reddot password reset";
            String body = "Hi there,\n\n" +
                    "To reset your password, click the link below:\n"
                    + fullUrl + "/reset-password?token=" + recoveryToken.getToken();
            mailSenderManager.sendEmail(email, subject, body);

            // save recovery token
            recoveryTokenRepository.save(recoveryToken);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Set<Role> getRolesByString(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        try {
            if (strRoles == null || strRoles.isEmpty()) {
                roles.add(findRoleByName(ROLENAME.ROLE_USER));
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "ROLE_ADMIN":
                            roles.add(findRoleByName(ROLENAME.ROLE_ADMIN));
                            break;
                        case "ROLE_MODERATOR":
                            roles.add(findRoleByName(ROLENAME.ROLE_MODERATOR));
                            break;
                        default:
                            roles.add(findRoleByName(ROLENAME.ROLE_USER));
                            break;
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    private Role findRoleByName(ROLENAME roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }


    private List<String> validateUser(RegisterRequest user) {
        List<String> messages = new ArrayList<>();
        if (!Validator.isUsernameValid(user.getUsername())) {
            messages.add("Invalid Username Format");
        } else if (userRepository.existsByUsername(user.getUsername())) {
            messages.add("Username already exists in the system!");
        }

        if (!Validator.isEmailValid(user.getEmail())) {
            messages.add("Invalid Email Format");
        } else if (userRepository.existsByEmail(user.getEmail())) {
            messages.add("Email already exists in the system");
        }

        if (!Validator.isPasswordValid(user.getPassword())) {
            messages.add("Invalid Password Format");
        }

        return messages;
    }
}

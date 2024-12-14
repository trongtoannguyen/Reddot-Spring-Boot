package com.reddot.app.service.user;

import com.reddot.app.assembler.UserAssembler;
import com.reddot.app.dto.UserProfileDTO;
import com.reddot.app.dto.request.ProfileUpdateRequest;
import com.reddot.app.dto.request.RegisterRequest;
import com.reddot.app.dto.request.UpdatePasswordRequest;
import com.reddot.app.entity.*;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.exception.EmailNotFoundException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.*;
import com.reddot.app.service.email.MailSenderManager;
import com.reddot.app.util.Validator;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceManagerImp implements UserServiceManager {
    private final PasswordEncoder encoder;
    private final MailSenderManager mailSenderManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final RecoveryTokenRepository recoveryTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final userDeleteRepository userDeleteRepository;
    private final String fullUrl;

    public UserServiceManagerImp(@Value("${server.address}") String appDomain,
                                 @Value("${server.port}") String appPort,
                                 @Value("${server.servlet.context-path}") String appPath,
                                 MailSenderManager mailSenderManager, UserRepository userRepository, RoleRepository roleRepository,
                                 PasswordEncoder encoder, RecoveryTokenRepository recoveryTokenRepository,
                                 ConfirmationTokenRepository confirmationTokenRepository, PersonRepository personRepository,
                                 userDeleteRepository userDeleteRepository) {
        this.mailSenderManager = mailSenderManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.recoveryTokenRepository = recoveryTokenRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.fullUrl = "http://" + appDomain + ":" + appPort + appPath;
        this.personRepository = personRepository;
        this.userDeleteRepository = userDeleteRepository;
    }


    /**
     * Locates the user based on the username or email.
     *
     * @param param the username/email identifying the user whose data is required.
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String param) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(param, param)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + param));
    }

    // TODO: comment out these methods to implement User entity instead of UserDetails
    // protected List<GrantedAuthority> loadUserAuthorities(String username)
    // Helper method
    // protected UserDetails createUserDetails(User userFromDb, List<GrantedAuthority> combinedAuthorities)

    @Override
    public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("User not found with mail address: " + email));
    }

    /**
     * Every user must be User role when created, EVERYONE IS EQUAL
     */
    @Override
    public void userCreate(RegisterRequest request) {
        try {
            List<String> errorMessages = validateUser(request);
            if (!errorMessages.isEmpty()) {
                log.error(String.valueOf(errorMessages));
                throw new Exception(String.valueOf(errorMessages));
            }
            User user = new User(request.getUsername(), request.getEmail(),
                    encoder.encode(request.getPassword())
            );

            ROLENAME roleUser = ROLENAME.ROLE_USER;
            Role role = findRoleByName(roleUser);
            user.addRole(role);
            userRepository.save(user);

            // Send confirmation email
            Assert.notNull(user.getId(), "User id must not be null");
            ConfirmationToken token = new ConfirmationToken(user.getId());
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
    public User userConfirm(String token) {
        try {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("TOKEN_NOT_FOUND"));
            if (confirmationToken.getConfirmedAt() != null) {
                throw new Exception("EMAIL_ALREADY_CONFIRMED");
            }
            User user = getUser(confirmationToken.getOwnerId());
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Create user profile
            Person person = new Person(user.getUsername());
            user.setPerson(person);
            userRepository.save(user);

            // update confirm token
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
    public void userDeleteRequest(Integer userId) throws ResourceNotFoundException {
        try {
            if (userDeleteRepository.existsByUserId(userId)) {
                throw new Exception("USER ALREADY IN DELETE QUEUE");
            }
            User user = getUser(userId);
            UserOnDelete userOnDelete = new UserOnDelete(userId);
            userDeleteRepository.save(userOnDelete);

            // send warning mail
            String subject = "Reddot account deletion";
            String body = """
                    Hi there,
                                       \s
                    Your account has been marked for deletion. If you did not request this, please contact us immediately.""";
            mailSenderManager.sendEmail(user.getEmail(), subject, body);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void userOnLoginUpdate(@NonNull String email) {
        try {
            User user = getUserByEmail(email);
            user.setLastAccess(LocalDateTime.now());
            userRepository.save(user);

            // remove delete request queue if exists
            if (userDeleteRepository.existsByUserId(user.getId())) {
                userDeleteRepository.removeByUserId(user.getId());
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void emailUpdate(Integer userId, String newEmail) throws ResourceNotFoundException {
        try {
            User user = getUser(userId);
            if (userExistsByEmail(newEmail)) {
                if (user.getEmail().equals(newEmail) && !user.isEmailVerified()) {
                    throw new Exception("Please check your mail box or spam folder to confirm your email." +
                            " Or you may need to resend the confirmation email.");
                }
                throw new Exception("EMAIL_ALREADY_EXISTS");
            }
            user.setEmail(newEmail);
            user.setEmailVerified(false);
            userRepository.save(user);

            // Send mail confirm
            ConfirmationToken confirmationToken = new ConfirmationToken(user.getId());
            String subject = "Reddot email confirmation";
            String body = "Hi there,\n\n" +
                    "To confirm your new email, click the link below:\n"
                    + fullUrl + "/settings/email/confirm?token=" + confirmationToken.getToken();
            mailSenderManager.sendEmail(newEmail, subject, body);

            // Save confirmation token
            confirmationTokenRepository.save(confirmationToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void emailConfirm(@NonNull String token) throws ResourceNotFoundException {
        try {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("TOKEN_NOT_FOUND"));
            if (confirmationToken.getConfirmedAt() != null) {
                throw new Exception("TOKEN_ALREADY_USED");
            }
            if (confirmationToken.getValidBefore().isBefore(LocalDateTime.now())) {
                throw new Exception("TOKEN_EXPIRED");
            }
            User user = getUser(confirmationToken.getOwnerId());
            user.setEmailVerified(true);
            userRepository.save(user);

            // update confirm token
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void emailConfirmResend(Integer userId) throws ResourceNotFoundException {
        try {
            User user = getUser(userId);
            if (user.isEmailVerified()) {
                throw new Exception("EMAIL_ALREADY_CONFIRMED");
            }

            // Send mail confirm
            ConfirmationToken confirmationToken = new ConfirmationToken(user.getId());
            String subject = "Reddot email confirmation";
            String body = "Hi there,\n\n" +
                    "To confirm your email, click the link below:\n"
                    + fullUrl + "/settings/email/confirm?token=" + confirmationToken.getToken();
            mailSenderManager.sendEmail(user.getEmail(), subject, body);

            // Save confirmation token
            confirmationTokenRepository.save(confirmationToken);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pwForgot(String email) throws ResourceNotFoundException {
        try {
            if (!userExistsByEmail(email)) {
                throw new ResourceNotFoundException("EMAIL_NOT_FOUND");
            }
            User user = getUser(email);

            // send password reset email
            RecoveryToken recoveryToken = new RecoveryToken(user.getId());
            String subject = "Reddot password reset";
            String body = "Hi there,\n\n" +
                    "To reset your password, click the link below:\n"
                    + fullUrl + "/settings/reset-password?token=" + recoveryToken.getToken();
            mailSenderManager.sendEmail(email, subject, body);

            // save recovery token
            recoveryTokenRepository.save(recoveryToken);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pwReset(UpdatePasswordRequest request) {
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
            User user = getUser(recoveryToken.getOwnerId());
            user.setPassword(encoder.encode(request.getPassword()));
            userRepository.save(user);
            recoveryToken.setUsed(true);
            recoveryTokenRepository.save(recoveryToken);

            // send email
            String subject = "Reddot password reset successful";
            String body = """
                     Hi there,
                                        \s
                     Your password has been reset successfully.
                    \s""";
            mailSenderManager.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserProfileDTO profileGetById(Integer userId) {
        try {
            User user = getUser(userId);
            return getUserProfileDTO(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserProfileDTO profileGetByUsername(String username) {
        try {
            User user = getUserByUsername(username);
            return getUserProfileDTO(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserProfileDTO profileUpdate(Integer userId, @Valid ProfileUpdateRequest updateRequest) {
        try {
            User user = getUser(userId);
            Person person = personRepository.findByUserId(user.getId()).orElse(new Person(user.getUsername()));
            user.setAvatarUrl(updateRequest.getAvatar());

            // Update person from DTO
            updateRequest.updateProfile(user, person);
            userRepository.save(user);
            log.info("User profile updated successfully");

            return UserAssembler.toUserProfileDTO(user, person);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserProfileDTO getUserProfileDTO(User user) {
        Optional<Person> p = personRepository.findByUserId(user.getId());
        Person person;

        if (p.isPresent()) {
            person = p.get();
        } else {
            person = new Person(user.getUsername());
            user.setPerson(person);
            userRepository.save(user);
        }

        return UserAssembler.toUserProfileDTO(user, person);
    }

    private boolean userExistsByEmail(String email) {
        Assert.notNull(email, "Email is null");
        return userRepository.findByEmail(email).isPresent();
    }

    private User getUser(Integer ownerId) {
        return userRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
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

package com.reddot.app.service.user;

import com.reddot.app.authentication.dto.RegisterRequest;
import com.reddot.app.entity.Role;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.repository.RoleRepository;
import com.reddot.app.repository.UserRepository;
import com.reddot.app.util.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
public class UserServiceManagerImp implements UserServiceManager {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    public UserServiceManagerImp(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void createUser(UserDetails userDetails) {
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
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

    protected UserDetails createUserDetails(User userFromDb, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromDb.getUsername();
        return new org.springframework.security.core.userdetails.User(returnUsername, userFromDb.getPassword(), userFromDb.isEnabled(), userFromDb.isAccountNonExpired(), userFromDb.isCredentialsNonExpired(), userFromDb.isAccountNonLocked(), combinedAuthorities);
    }

    @Override
    public User createNewUser(RegisterRequest request) {
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
            user.setCreatedBy(user.getUsername());

            Set<Role> roles = getRolesByString(request.getRoles());
            user.setRoles(roles);

            userRepository.save(user);
            return user;
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

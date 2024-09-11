package com.reddot.app.service.user;

import com.reddot.app.entity.User;
import com.reddot.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceManagerImp implements UserServiceManager {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceManagerImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDetails user) {

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
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
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

    protected UserDetails createUserDetails(User userFromDb,
                                            List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromDb.getUsername();
        return new org.springframework.security.core.userdetails.User(returnUsername, userFromDb.getPassword(), userFromDb.isEnabled(),
                userFromDb.isAccountNonExpired(), userFromDb.isCredentialsNonExpired(),
                userFromDb.isAccountNonLocked(), combinedAuthorities);
    }
}

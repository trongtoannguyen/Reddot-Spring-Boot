package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @NaturalId
    @NonNull
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String username;

    @NaturalId
    @NonNull
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NonNull
    @Size(min = 6, max = 100)
    private String password;

    @Lob
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OrderBy("name ASC")
    Set<Role> roles = new HashSet<>();

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "FK_USER_PERSON"))
    private Person person;

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this(username, email, password, true, true, true);
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, boolean accountNonExpired, boolean accountNonLocked, boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }
}
package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddot.app.entity.enumeration.ROLENAME;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class User extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private static final Collection<? extends Role> DEFAULT_ROLES = Set.of(new Role(ROLENAME.ROLE_USER));

    @NaturalId
    @NonNull
    @Column(unique = true)
    private String username;

    @NaturalId
    @NonNull
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NonNull
    private String password;

    @Lob
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
    @OrderBy("name ASC")
    private Set<Role> roles = new HashSet<>();

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Person person;

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.enabled = true;
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, Set<Role> roles) {
       this(username, email, password, true, true, true, roles);
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, boolean accountNonExpired, boolean accountNonLocked, boolean enabled, Collection<? extends Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.enabled = enabled;
        this.roles = new HashSet<>(roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }
}
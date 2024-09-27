package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The User class is an entity model object. It represents sensitive information of person account.
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name = "users")
public class User extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @NaturalId
    @NonNull
    @Column(unique = true)
    private String username;

    @NaturalId
    @NonNull
    @Column(name = "email_hash", unique = true)
    private String email;

    @JsonIgnore
    @NonNull
    @Column(name = "password_hash")
    private String password;

    private boolean isVerified;

    private boolean isEnabled;

    @Lob
    private String avatarUrl;

    private String provider;

    // it’s unusual to consider the User as a client-side and the Person as the parent-side
    // because the person cannot exist without an actual user.
    // So the User entity should be the parent-side and the Person entity should be the client-side.
    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Person person;

    @ManyToMany(cascade = {
            CascadeType.REFRESH,
            CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Notification> notifications;

    // Question should not be deleted
    @OneToMany(mappedBy = "user",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH
            })
    private List<Question> questions;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserOAuth> userOAuths = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JwtToken> jwtTokens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBadge> userBadges = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnabled = true;
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, Set<Role> roles) {
        this(username, email, password, true, roles);
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, boolean isEnabled, Collection<? extends Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.roles = new HashSet<>(roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }

    // Utility methods
    public void addPerson(Person person) {
        this.person = person;
        person.setUser(this);
    }

    public void removePerson() {
        if (person != null) {
            person.setUser(null);
            this.person = null;
        }
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setUser(this);
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setUser(null);
    }

    public void addBadge(Badge badge) {
        this.userBadges.add(new UserBadge(this, badge));
        badge.getUserBadges().add(new UserBadge(this, badge));
    }

    public void removeBadge(Badge badge) {
        this.userBadges.removeIf(userBadge -> userBadge.getBadge().equals(badge));
        badge.getUserBadges().removeIf(userBadge -> userBadge.getUser().equals(this));
    }

    public void follow(User followed) {
        Follow follow = new Follow(this, followed);
        followings.add(follow);
        followed.getFollowers().add(follow);
    }

    public void unfollow(User followed) {
        Follow follow = new Follow(this, followed);
        followings.remove(follow);
        followed.getFollowers().remove(follow);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setUser(this);
    }

    // When user is deleted, dissociate all questions in DB
    @PreRemove
    public void removeQuestionsAssociated() {
        for (Question question : questions) {
            question.setUser(null);
        }
        this.questions.clear();
    }

    public void addBookmark(Bookmark bookmark) {
        this.bookmarks.add(bookmark);
        bookmark.setUser(this);
    }

    public void removeBookmark(Bookmark bookmark) {
        this.bookmarks.remove(bookmark);
        bookmark.setUser(null);
    }

    public void addUserOAuth(UserOAuth userOAuth) {
        this.userOAuths.add(userOAuth);
        userOAuth.setUser(this);
    }

    public void removeUserOAuth(UserOAuth userOAuth) {
        this.userOAuths.remove(userOAuth);
        userOAuth.setUser(null);
    }

    public void addJwtToken(JwtToken jwtToken) {
        this.jwtTokens.add(jwtToken);
        jwtToken.setUser(this);
    }

    public void removeJwtToken(JwtToken jwtToken) {
        this.jwtTokens.remove(jwtToken);
        jwtToken.setUser(null);
    }
}
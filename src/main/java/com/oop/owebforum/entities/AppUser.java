package com.oop.owebforum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String password;
    private String email;
    private String username;
    private int karma;

    private LocalDate dateOfRegistration;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities;
    private boolean locked;
    private boolean enabled;

    public AppUser(){
        super();
        this.authorities = new HashSet<Role>();
    }

    public AppUser(String password,
                   String email,
                   String username,
                   int karma,
                   LocalDate dateOfRegistration,
                   Set<Role> authorities,
                   boolean locked,
                   boolean enabled) {
        this.password = password;
        this.email = email;
        this.username = username;
        this.karma = karma;
        this.dateOfRegistration = dateOfRegistration;
        this.authorities = authorities;
        this.locked = locked;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

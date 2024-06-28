package com.oop.owebforum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-increment columns
    @Column(name = "role_id")
    private Integer roleId;

    private String authority;

    public Role(String authority){
        this.authority = authority;
    }

    public Role(Integer roleId, String authority){
        this.authority = authority;
        this.roleId = roleId;
    }

}

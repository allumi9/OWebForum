package com.oop.owebforum.entities.dto;

import com.oop.owebforum.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String jwt;

    private String username;
    private List<String> roles;

    public LoginResponse(String jwt, String username, List<String> roles) {
        this.jwt = jwt;
        this.username = username;
        this.roles = roles;
    }
}

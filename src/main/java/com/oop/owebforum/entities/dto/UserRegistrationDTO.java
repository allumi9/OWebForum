package com.oop.owebforum.entities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
    private String password;
    private String username;
    private String email;
}

package com.oop.owebforum.services;

import com.oop.owebforum.entities.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    public String register(RegistrationRequest request){
        return "works";
    }
}

package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.dto.UserRegistrationDTO;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.RoleRepository;
import com.oop.owebforum.services.RegistrationService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.oop.owebforum.entities.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
@NoArgsConstructor
public class RegistrationController {

    private AppUserRepository appUserRepository;
    private RegistrationService registrationService;
    @Autowired
    public RegistrationController(AppUserRepository appUserRepository,
                                  RegistrationService registrationService){
        this.appUserRepository = appUserRepository;
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO userRegistrationDTO){

        if(appUserRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()){ // знову є редайрект тому лишаю в контродері
            return "redirect:/register?error=usernameTaken";
        }

        registrationService.registerUser(userRegistrationDTO);

        return "redirect:/login";
    }

}

package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.dto.UserRegistrationDTO;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.RoleRepository;
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
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationController(AppUserRepository appUserRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder){
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO userRegistrationDTO){
        if(appUserRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()){
            return "redirect:/register?error=usernameTaken";
        }
        Set<Role> roleSet = new HashSet<>();
        if(roleRepository.findByAuthority("USER").isPresent()){
            roleSet.add(roleRepository.findByAuthority("USER").get());
        }


        AppUser appUser = new AppUser(passwordEncoder.encode(userRegistrationDTO.getPassword()),
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getUsername(),
                0, LocalDate.now(), roleSet, false, true);
        appUserRepository.save(appUser);

        return "redirect:/login";
    }

}

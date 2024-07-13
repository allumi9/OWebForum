package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Role;
import com.oop.owebforum.entities.dto.UserRegistrationDTO;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationService(AppUserRepository appUserRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder){
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegistrationDTO userRegistrationDTO){
        Set<Role> roleSet = new HashSet<>();
        if(roleRepository.findByAuthority("USER").isPresent()){
            roleSet.add(roleRepository.findByAuthority("USER").get());
        }

        AppUser appUser = new AppUser(passwordEncoder.encode(userRegistrationDTO.getPassword()),
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getUsername(),
                0, LocalDate.now(), roleSet, false, true);
        appUserRepository.save(appUser);
    }

}

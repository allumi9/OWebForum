package com.oop.owebforum.services;

import com.oop.owebforum.entities.Role;
import com.oop.owebforum.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles(){
        if(roleRepository.count() == 0){
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("MODERATOR"));
        }
    }

}

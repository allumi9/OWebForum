package com.oop.owebforum;
import com.oop.owebforum.controllers.PostController;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.Role;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.CategoryRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {

    @Bean
    CommandLineRunner run(RoleRepository roleRepository,
                          AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder,
                          PostRepository postRepository,
                          PostController postController,
                          CategoryRepository categoryRepository){
        return args -> {
            if(appUserRepository.findByUsername("admin").isPresent()){
                return;
            }

            Role adminRole = roleRepository.findByAuthority("ADMIN").orElseThrow(() -> new IllegalStateException("Admin role not found"));
            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new IllegalStateException("User role not found"));

            Set<Role> roles = new HashSet<>();

            roles.add(adminRole);
            roles.add(userRole);

            AppUser admin = new AppUser(
                    passwordEncoder.encode("1"),
                    "max@gmail",
                    "admin",
                    4, LocalDate.now(), roles, false, true);
            appUserRepository.save(admin);

            Category category = categoryRepository.findByName("Politics").get();

            Post post = new Post(1, admin, "Politics are boring!!!",
                    "boo!! fuck politics!!!",
                    category,
                    LocalDateTime.now(),
                    0);
            postRepository.save(post);

        };
    }
}

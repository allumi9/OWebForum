package com.oop.owebforum;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Role;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.logging.Logger;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.rmi.server.LogStream.log;

@SpringBootApplication
public class OWebForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(OWebForumApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()){
				return;
			}
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			AppUser admin = new AppUser("password",
					"max@gmail",
					"admin",
					4, LocalDate.now(), roles, false, true);
			appUserRepository.save(admin);



			log("the init is done");

		};
	}

}

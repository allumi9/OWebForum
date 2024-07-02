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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class OWebForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(OWebForumApplication.class, args);
	}

}

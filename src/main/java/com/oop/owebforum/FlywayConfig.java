package com.oop.owebforum;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;

@Configuration
public class FlywayConfig {

    @Bean
    public CommandLineRunner cleanAndMigrate(Flyway flyway) {
        return args -> {
            flyway.clean();
            flyway.migrate();
        };
    }

}

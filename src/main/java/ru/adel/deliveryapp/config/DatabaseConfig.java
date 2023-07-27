package ru.adel.deliveryapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.adel.deliveryapp.repositories")
public class DatabaseConfig {
}

package com.example.api.clientes.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.api.clientes.domain.repository")
public class PersistenceConfiguration {
}

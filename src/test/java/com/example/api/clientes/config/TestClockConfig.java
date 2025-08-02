package com.example.api.clientes.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@TestConfiguration
public class TestClockConfig {
    @Primary
    @Bean
    public Clock clock() {
        return Clock.fixed(LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant(), ZoneId.of("America/Sao_Paulo"));
    }
}
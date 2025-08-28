package com.example.api.clientes.integration.controller;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PessoaFisicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @TestConfiguration
    static class TestClockConfig {
        @Primary
        @Bean
        public Clock clock() {
            // O relógio agora está fixo em 01/01/2025 para os testes
            return Clock.fixed(
                    LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant(),
                    ZoneId.of("America/Sao_Paulo")
            );
        }
    }

    @Test
    void deveCadastrarPessoaFisica() throws Exception {
        PessoaFisicaRequest pessoaArranjo = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2007, 1, 1));

        var mvcResult = mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaArranjo)))
                .andExpect(status().isCreated())
                .andReturn();
        var result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PessoaFisicaResponse.class);
        Assertions.assertTrue(result.id() > 0, "ID deve ser maior que zero");
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveBuscarPessoaFisicaPorId() throws Exception {
        PessoaFisicaRequest pessoaArranjo = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2000, 1, 1));

        mockMvc.perform(get("/v1/clientes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value(pessoaArranjo.nome()))
                .andExpect(jsonPath("$.cpf").value(pessoaArranjo.cpf()))
                .andExpect(jsonPath("$.dataNascimento").value("2000-01-01"));

    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveBuscarPessoaFisicaPorCpf() throws Exception {
        PessoaFisicaRequest pessoaArranjo = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2000, 1, 1));

        mockMvc.perform(get("/v1/clientes/cpf/{number}", pessoaArranjo.cpf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value(pessoaArranjo.nome()))
                .andExpect(jsonPath("$.cpf").value(pessoaArranjo.cpf()));
    }

    @Test
    void deveRetornar422QuandoPessoaMenorDeIdade() throws Exception {
        var request = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2007, 4, 2));
        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveRetornar422QuandoCpfDuplicado() throws Exception {
        var request = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2000, 1, 1));

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}

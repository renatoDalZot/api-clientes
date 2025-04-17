package com.example.api.clientes.integration.controller;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
//@Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PessoaFisicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void deveCriarPessoaFisica() throws Exception {
        PessoaFisicaRequest pessoaFisica = new PessoaFisicaRequest("João", "12345678900", LocalDate.of(2025,1,1), LocalDate.of(2000, 1, 1));

        mockMvc.perform(post("/v1/pessoa-fisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaFisica)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void deveCadastrarPessoaFisica() throws Exception {
        PessoaFisicaRequest pessoaFisica = new PessoaFisicaRequest("João", "12345678900", LocalDate.of(2025,1,1), LocalDate.of(2000, 1, 1));
        var mvcResult = mockMvc.perform(post("/v1/pessoa-fisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaFisica)))
                .andExpect(status().isCreated())
                .andReturn();
        var result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PessoaFisicaResponse.class);
        Assertions.assertTrue(result.id() > 0, "ID deve ser maior que zero");
    }

}

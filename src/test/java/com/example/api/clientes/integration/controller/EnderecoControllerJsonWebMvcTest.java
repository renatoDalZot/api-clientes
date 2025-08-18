package com.example.api.clientes.integration.controller;

import com.example.api.clientes.application.service.EnderecoService;
import com.example.api.clientes.application.service.PessoaFisicaService;
import com.example.api.clientes.infraestrutura.EnderecoConsulta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ActiveProfiles("test")
class EnderecoControllerJsonWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PessoaFisicaService pessoaFisicaService;

    @MockitoBean
    private EnderecoService enderecoService;

    @MockitoBean
    private EnderecoConsulta enderecoConsulta;

    @Test
    void quandoEnderecoNaoEncontradoDeveRetornar404() throws Exception {
        // Simulando o comportamento do serviço para retornar Optional.empty()
        when(enderecoConsulta.findByCpf("12345678900")).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/clientes/{cpf}/endereco", "12345678900"))
                .andExpect(status().isNotFound());
    }
}
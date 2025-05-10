package com.example.api.clientes.integration.controller;


import com.example.api.clientes.application.PessoaFisicaService;
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
class PessoaFisicaControllerWebMvcTest {



    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PessoaFisicaService service;

    @Test
    void quandoIdInexistenteDeveRetornar404() throws Exception {
        // Simulando o comportamento do serviço para retornar Optional.empty()
        when(service.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/pessoa-fisica/{id}", 1))
                .andExpect(status().isNotFound());
    }


}

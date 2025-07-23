package com.example.api.clientes.integration.controller;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.domain.model.Endereco;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "configuracao.endereco.formato=text")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EnderecoControllerTextTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveCadastrarEndereco() throws Exception {
        //Cenário
        String enderecoRequest = String.join(System.lineSeparator(),
                "Rua Riberão Tavares, 123 - Casa",
                "Bom Jesus",
                "12345-678",
                "São Paulo/SP");

        // Ação
        var mvcResult = mockMvc.perform(post("/v1/clientes/{cpf}/endereco", "12345678900")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoRequest))
                .andExpect(status().isCreated())
                .andReturn();
        var result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EnderecoResponse.class);

        // Verificação
        Assertions.assertTrue(result.id() > 0, "ID deve ser maior que zero");

        var enderecoPersistido = entityManager.find(Endereco.class, result.id());
        Assertions.assertNotNull(enderecoPersistido, "O endereço deve estar persistido no banco de dados");
        Assertions.assertEquals("Rua Riberão Tavares", enderecoPersistido.getLogradouro());
        Assertions.assertEquals("123", enderecoPersistido.getNumero());
        Assertions.assertEquals("12345-678", enderecoPersistido.getCep());
        Assertions.assertEquals("Casa", enderecoPersistido.getComplemento());
        Assertions.assertEquals("Bom Jesus", enderecoPersistido.getBairro());
        Assertions.assertEquals("São Paulo", enderecoPersistido.getCidade());
        Assertions.assertEquals("SP", enderecoPersistido.getEstado());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveRetornar404QuandoPessoaInexistente() {
        // Cenário
        EnderecoRequest enderecoRequest = new EnderecoRequest("Avenida Paulista", "1578", "Apto 101", "Bela Vista",
                "01310-200", "São Paulo", "SP");

        // Ação e Verificação
        try {
            mockMvc.perform(post("/v1/clientes/{cpf}/endereco", "12345678901")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(enderecoRequest)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            Assertions.fail("Erro ao cadastrar endereço: " + e.getMessage());
        }
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_endereco.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveBuscarEnderecoPorCpf() {
        // Cenário
        String cpf = "12345678900";

        // Ação e Verificação
        try {
            mockMvc.perform(get("/v1/clientes/{cpf}/endereco", cpf))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.logradouro").value("Rua Riberão Tavares"))
                    .andExpect(jsonPath("$.numero").value(123))
                    .andExpect(jsonPath("$.cep").value("12345-678"))
                    .andExpect(jsonPath("$.complemento").value("Casa"))
                    .andExpect(jsonPath("$.bairro").value("Bom Jesus"))
                    .andExpect(jsonPath("$.cidade").value("São Paulo"))
                    .andExpect(jsonPath("$.estado").value("SP"));
        } catch (Exception e) {
            Assertions.fail("Erro ao buscar endereço: " + e.getMessage());
        }
    }

    @Test
    void deveRetornar404QuandoEnderecoInexistente() {
        // Cenário
        String cpf = "12345678900";

        // Ação e Verificação
        try {
            mockMvc.perform(get("/v1/clientes/{cpf}/endereco", cpf))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            Assertions.fail("Erro ao buscar endereço: " + e.getMessage());
        }
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/inserir_pessoa_fisica.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/limpar_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void deveRetornar422QuandoCepInvalido() throws Exception {
        // Cenário, ação e verificação
        mockMvc.perform(post("/v1/clientes/{cpf}/endereco", "12345678900")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {
                                "logradouro": "Avenida Paulista",
                                "numero": 1578,
                                "complemento": "Apto 101",
                                "bairro": "Bela Vista",
                                "cep": "CEP_INVALIDO",
                                "cidade": "São Paulo",
                                "estado": "SP"
                            }
                        """
                        ))
                .andExpect(status().isUnprocessableEntity());
    }
}
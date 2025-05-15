package com.example.api.clientes.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequest(
        @NotEmpty (message = "Logradouro não pode ser vazio")
        String logradouro,
        @NotNull(message = "Número não pode ser nulo")
        Integer numero,
        String complemento,
        @NotEmpty (message = "Bairro não pode ser vazio")
        String bairro,
        @NotEmpty (message = "CEP não pode ser vazio")
        String cep,
        @NotEmpty (message = "Cidade não pode ser vazio")
        String cidade,
        @NotEmpty (message = "Estado não pode ser vazio")
        String estado
){
    public EnderecoRequest {
        if (!cep.matches("\\d{5}-\\d{3}")) throw new IllegalArgumentException("CEP deve estar no formato 00000-000");
    }
}


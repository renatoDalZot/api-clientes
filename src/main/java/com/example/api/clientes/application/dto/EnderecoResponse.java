package com.example.api.clientes.application.dto;

public record EnderecoResponse(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cep,
        String cidade,
        String estado
) {}

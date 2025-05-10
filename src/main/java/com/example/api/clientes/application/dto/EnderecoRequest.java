package com.example.api.clientes.application.dto;

public record EnderecoRequest(
        String logradouro,
        Integer numero,
        String complemento,
        String bairro,
        String cep,
        String cidade,
        String estado
){}


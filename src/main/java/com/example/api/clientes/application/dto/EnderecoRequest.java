package com.example.api.clientes.application.dto;


public record EnderecoRequest(Long pessoaFisicaId,
                              String logradouro,
                              Integer numero,
                              String complemento,
                              String bairro,
                              String cep,
                              String municipio,
                              String uf) {}

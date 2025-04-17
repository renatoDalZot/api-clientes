package com.example.api.clientes.application.dto;


import java.time.LocalDate;

public record PessoaFisicaRequest(String nome,
                                  String cpf,
                                  LocalDate dataCadastro,
                                  LocalDate dataNascimento) {
}

package com.example.api.clientes.application.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record PessoaFisicaResponse(Long id,
                                   String nome,
                                   String cpf,
                                   LocalDate dataCadastro,
                                   LocalDate dataNascimento,
                                   BigDecimal rendaMensal,
                                   Double score) {
}

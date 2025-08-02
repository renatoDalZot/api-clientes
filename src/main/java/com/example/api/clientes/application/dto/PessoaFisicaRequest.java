package com.example.api.clientes.application.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PessoaFisicaRequest(
        @NotEmpty(message = "Nome é obrigatório")
        String nome,
        @NotEmpty(message = "CPF é obrigatório")
        String cpf,
        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate dataNascimento
) {
}

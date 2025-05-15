package com.example.api.clientes.helper;

import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.domain.model.PessoaFisica;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PessoaFisicaBuilder {
    private String nome = "João da Silva";
    private String cpf = "000.000.000-00";
    private LocalDate dataCadastro = LocalDate.of(2025, 4, 1);
    private LocalDate dataNascimento = LocalDate.of(2007, 4, 1);

    public PessoaFisicaBuilder withNome(String nome) {
        this.nome = nome;
        return this;
    }

    public PessoaFisicaBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public PessoaFisicaBuilder withDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public PessoaFisicaBuilder withDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public PessoaFisica build() {
        return new PessoaFisica(nome, cpf, dataCadastro, dataNascimento);
    }

    public PessoaFisicaResponse buildResponse() {
        return new PessoaFisicaResponse(
                1L,
                nome,
                cpf,
                dataCadastro,
                dataNascimento,
                BigDecimal.valueOf(0.0),
                0.0
        );
    }
}

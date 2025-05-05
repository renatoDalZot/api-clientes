package com.example.api.clientes.unit.provider;

import com.example.api.clientes.domain.model.PessoaFisica;

import java.time.LocalDate;

public class PessoaFisicaProvider {

    public static PessoaFisica pessoaFisicaComDatasEspecificas(LocalDate dataCadastro, LocalDate dataNascimento) {
        return new PessoaFisica("João da Silva", "12345678900", dataCadastro, dataNascimento);
    }
}

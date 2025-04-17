package com.example.api.clientes.unit.util;

import com.example.api.clientes.domain.model.PessoaFisica;

import java.time.LocalDate;

public class GeradorEntidadesTeste {

    public static PessoaFisica pessoaFisicaComDatasEspecificas(LocalDate dataCadastro, LocalDate dataNascimento) {
        return new PessoaFisica("João da Silva", "12345678900", dataCadastro, dataNascimento);
    }

    public static PessoaFisica pessoaFisicaMaiorDeIdade() {
        return pessoaFisicaComDatasEspecificas(LocalDate.of(2025, 4, 5), LocalDate.of(2000, 1, 1));
    }

    public static PessoaFisica pessoaFisicaMenorDeIdade() {
        return pessoaFisicaComDatasEspecificas(LocalDate.of(2025, 4, 5), LocalDate.of(2010, 1, 1));
    }


}

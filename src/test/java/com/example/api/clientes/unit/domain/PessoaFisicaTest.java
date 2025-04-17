package com.example.api.clientes.unit.domain;

import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.unit.util.GeradorEntidadesTeste;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PessoaFisicaTest {

    @ParameterizedTest(name = "{5}")
    @MethodSource(value = "com.example.api.clientes.unit.util.ProvedorDeDadosTeste#fornecerCenariosParaTesteDaRegraCriacaoMenorDeIdade")
    void deveImpedirCriacaoMenorDeIdade(LocalDate dataCadastro, LocalDate dataNascimento, boolean resultadoEsperado) {
        Exception exception = null;
        PessoaFisica pessoa = null;
        try {
            pessoa = GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas(dataCadastro, dataNascimento);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        if (resultadoEsperado) {
            assertNull(exception, "Resultado esperado: " + resultadoEsperado + ", mas o resultado foi: " +
                    exception);
            assertNotNull(pessoa, "Resultado esperado: " + resultadoEsperado + ", mas o resultado foi: " +
                    pessoa);
            assertEquals("João da Silva", pessoa.getNome(), "Resultado esperado: " + resultadoEsperado +
                    ", mas o resultado foi: " + pessoa);
        } else {
            assertNotNull(exception, "Resultado esperado: " + resultadoEsperado + ", mas o resultado foi: " +
                    exception);
            assertEquals("IllegalArgumentException", exception.getClass().getSimpleName(),
                    "Resultado esperado: " + resultadoEsperado + ", mas o resultado foi: " + exception);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "com.example.api.clientes.unit.util.ProvedorDeDadosTeste#fornecerCenariosParaAlteracaoDataNascimento")
    void deveImpedirAlteracaoDataNascimentoInvalida (LocalDate dataCadastro, LocalDate dataNascimentoOriginal,
                                                     LocalDate dataNascimentoNova, boolean resultadoEsperado) {
        var pessoa  = GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas(dataCadastro, dataNascimentoOriginal);

        Exception exception = null;
        try {
            pessoa.setDataNascimento(dataNascimentoNova);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        if (resultadoEsperado) {
            assertNull(exception, "Resultado esperado: true, mas o resultado foi: " +
                    exception);
            assertEquals(dataNascimentoNova, pessoa.getDataNascimento(), "Resultado esperado: true, mas o resultado foi: " + pessoa);
        } else {
            assertNotNull(exception, "Resultado esperado: false, mas o resultado foi: " +
                    exception);
            assertEquals("IllegalArgumentException", exception.getClass().getSimpleName(), "Resultado esperado: false, " +
                    "mas o resultado foi: " + exception);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "com.example.api.clientes.unit.util.ProvedorDeDadosTeste#fornecerCenariosParaTesteDaRegraCriacaoMenorDeIdade")
    void deveImpedirAlteracaoDatasInvalida (LocalDate dataCadastro, LocalDate dataNascimento, LocalDate dataNascimentoNova,
                                            boolean resultadoEsperado) {
        var pessoa  = GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas(dataCadastro, dataNascimento);

        Exception exception = null;
        try {
            pessoa.setDatas(dataCadastro, dataNascimentoNova);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        if (resultadoEsperado) {
            assertNull(exception, "Resultado esperado: true, mas o resultado foi: " + exception);
            assertEquals(dataCadastro, pessoa.getDataCadastro(), "Resultado esperado: true, mas o resultado foi: " + pessoa);
            assertEquals(dataNascimentoNova, pessoa.getDataNascimento(), "Resultado esperado: true, " +
                    "mas o resultado foi: " + pessoa);
        } else {
            assertNotNull(exception, "Resultado esperado: false, mas o resultado foi: " +
                    exception);
            assertEquals("IllegalArgumentException", exception.getClass().getSimpleName(),
                    "Resultado esperado: false, mas o resultado foi: " + exception);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "com.example.api.clientes.unit.util.ProvedorDeDadosTeste#fornecerCenariosParaAlteracaoDataCadastro")
    void deveImpedirAlteracaoDatacadastroInvalida (LocalDate dataCadastro, LocalDate dataNascimento,
                                                     LocalDate dataCadastroNova, boolean resultadoEsperado) {
        var pessoa  = GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas(dataCadastro,
                dataNascimento);

        Exception exception = null;
        try {
            pessoa.setDataCadastro(dataCadastro);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        if (resultadoEsperado) {
            assertNull(exception, "Resultado esperado: true, mas o resultado foi: " +
                    exception);
            assertEquals(dataCadastroNova, pessoa.getDataNascimento(), "Resultado esperado: true, " +
                    "mas o resultado foi: " + pessoa);
        } else {
            assertNotNull(exception, "Resultado esperado: false, mas o resultado foi: " +
                    exception);
            assertEquals("IllegalArgumentException", exception.getClass().getSimpleName(), "Resultado " +
                    "esperado: false, mas o resultado foi: " + exception);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "com.example.api.clientes.unit.util.ProvedorDeDadosTeste#fornecerCenariosParaTesteScore")
    void aoCriarDeveDefinirScore(LocalDate dataCadastro, LocalDate dataNascimento, double resultadoEsperado) {
        PessoaFisica pessoaFisica = GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas(dataCadastro, dataNascimento);

        double score = pessoaFisica.getScore();

        assertEquals(score, resultadoEsperado, "Resultado esperado: " + resultadoEsperado + ", mas o resultado foi: " + score);
    }
}

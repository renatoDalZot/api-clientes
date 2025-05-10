package com.example.api.clientes.unit.domain;

import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.helper.PessoaFisicaBuilder;
import com.example.api.clientes.unit.provider.PessoaFisicaProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PessoaFisicaTest {

    private static final LocalDate DATA_CADASTRO = LocalDate.of(2025, 4, 1);
    private static final LocalDate DATA_NASCIMENTO_MENOR_DE_IDADE = LocalDate.of(2007, 4, 2);
    private static final LocalDate DATA_NASCIMENTO_MAIOR_DE_IDADE = LocalDate.of(2007, 4, 1);

    @Test
    void dadoMenorDeIdadeQuandoInstanciadaPessoaFisicaDeveExcepcionar() {
        assertThrows(BusinessException.class, () -> new PessoaFisicaBuilder().withDataCadastro(DATA_CADASTRO)
                        .withDataNascimento(DATA_NASCIMENTO_MENOR_DE_IDADE).build());
    }

    @Test
    void dadoMaiorDeIdadeQuandoInstanciadaPessoaFisicaDeveSucesso() {
        assertDoesNotThrow(
                () -> new PessoaFisica("João", "123.456.789-00", DATA_CADASTRO, DATA_NASCIMENTO_MAIOR_DE_IDADE)
        );
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource(value = "fornecerCenariosParaTesteScore")
    void aoCriarPessoaDeveDefinirScoreCorreto(LocalDate dataCadastro, LocalDate dataNascimento, double scoreEsperado, String descricao) throws NoSuchFieldException, IllegalAccessException {
        PessoaFisica pessoaFisica = PessoaFisicaProvider.pessoaFisicaComDatasEspecificas(dataCadastro, dataNascimento);
        Field scoreField = PessoaFisica.class.getDeclaredField("score");
        scoreField.setAccessible(true);

        var score = (Double) scoreField.get(pessoaFisica);

        assertAll(
                () -> assertEquals(scoreEsperado, score,
                        "Resultado esperado: " + scoreEsperado + ", mas o resultado foi: " + score),
                () -> System.out.println("Teste passou: " + descricao + " | Esperado: " + scoreEsperado + " | Obtido: " + score)
        );
    }

    public static Stream<Arguments> fornecerCenariosParaTesteScore() {
        final var DATA_NASCIMENTO_IDADE_18 = LocalDate.of(2007, 4, 1);
        final var DATA_NASCIMENTO_IDADE_33 = LocalDate.of(1992, 4, 1);
        final var DATA_NASCIMENTO_IDADE_48 = LocalDate.of(1977, 4, 1);
        final var DATA_NASCIMENTO_IDADE_55 = LocalDate.of(1970, 4, 1);
        final var DATA_NASCIMENTO_IDADE_81 = LocalDate.of(1944, 4, 1);
        final var DATA_NASCIMENTO_IDADE_105 = LocalDate.of(1920, 4, 1);

        return Stream.of(
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_18, 5, "Cenário para idade 18, o score inicial é 5"),
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_33, 8, "Cenário para idade 33"),
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_48, 10, "Cenário para idade 48, o score não pode ser superior a 10"),
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_55, 9.5, "Cenário para idade 55, o score deve decrementar"),
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_81, 7, "Cenário para idade 81"),
                Arguments.of(DATA_CADASTRO, DATA_NASCIMENTO_IDADE_105, 5, "Cenário para idade 105, o score não pode ser superior a 5")
        );
    }
}

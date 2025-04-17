package com.example.api.clientes.unit.util;

import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.stream.Stream;

public class ProvedorDeDadosTeste {

    public static Stream<Arguments> fornecerCenariosParaTesteDaRegraCriacaoMenorDeIdade() {
        return Stream.of(
                Arguments.of(LocalDate.of(2025, 4, 4), LocalDate.of(1919, 6, 1), true),
                Arguments.of(LocalDate.of(2025, 2, 1), LocalDate.of(1964, 9, 12), true),
                Arguments.of(LocalDate.of(2024, 12, 1), LocalDate.of(1997, 4, 28), true),
                Arguments.of(LocalDate.of(2025, 3, 1), LocalDate.of(1990, 5, 5), true),
                Arguments.of(LocalDate.of(2025, 3, 1), LocalDate.of(2002, 6, 2), true),
                Arguments.of(LocalDate.of(2025, 4, 1), LocalDate.of(1977, 4, 1), true),
                Arguments.of(LocalDate.of(2007, 4, 5), LocalDate.of(2025, 4, 4), false),
                Arguments.of(LocalDate.of(2005, 7, 1), LocalDate.of(2021, 7, 1), false),
                Arguments.of(LocalDate.of(1998, 6, 30), LocalDate.of(2016, 6, 30), false)
        );
    }

    public static Stream<Arguments> fornecerCenariosParaAlteracaoDataNascimento() {
        return Stream.of(
                Arguments.of(LocalDate.of(2025, 4, 4),
                                LocalDate.of(1999, 6, 1), LocalDate.of(2013, 2, 20), false),
                        Arguments.of(LocalDate.of(2025, 2, 1),
                                LocalDate.of(2005, 9, 12), LocalDate.of(2009, 7, 15), false),
                        Arguments.of(LocalDate.of(2024, 12, 1),
                                LocalDate.of(2006, 4, 28), LocalDate.of(2005, 11, 25), true)
        );
    }

    public static Stream<Arguments> fornecerCenariosParaAlteracaoDataCadastro() {
        return Stream.of(
                Arguments.of(LocalDate.of(2025, 4, 4),
                        LocalDate.of(1999, 6, 1), LocalDate.of(2013, 2, 20), false),
                Arguments.of(LocalDate.of(2025, 2, 1),
                        LocalDate.of(2005, 9, 12), LocalDate.of(2009, 7, 15), false),
                Arguments.of(LocalDate.of(2024, 12, 1),
                        LocalDate.of(2004, 4, 28), LocalDate.of(2023, 11, 25), true)
        );
    }

    public static Stream<Arguments> fornecerCenariosParaTesteScore() {
        return Stream.of(
                Arguments.of(LocalDate.of(2025, 4, 4), LocalDate.of(1919, 6, 1), 5),
                Arguments.of(LocalDate.of(2025, 2, 1), LocalDate.of(1964, 9, 12), 9),
                Arguments.of(LocalDate.of(2024, 12, 1), LocalDate.of(1997, 4, 28), 6),
                Arguments.of(LocalDate.of(2025, 3, 1), LocalDate.of(1990, 5, 5), 8),
                Arguments.of(LocalDate.of(2025, 3, 1), LocalDate.of(2002, 6, 2), 5),
                Arguments.of(LocalDate.of(2016, 6, 30), LocalDate.of(1998, 6, 30), 5)
        );
    }
}

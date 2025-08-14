package com.example.api.clientes.unit.helper;

import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.helper.EnderecoParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

    class EnderecoParserTest {

        private static final String SEPARADOR_MUNICIPIO_UF = "/";
        private final EnderecoParser enderecoParser = new EnderecoParser(SEPARADOR_MUNICIPIO_UF);

        @ParameterizedTest(name = "{3}")
        @MethodSource("fornecerCenariosParaTesteDeveAnalisarEnderecoPlainTextEmDiversosCenarios")
        void deveAnalisarEnderecoPlainTextEmDiversosCenarios(String plainTextEndereco, String mensagemDeErro, Consumer<Endereco> consumidor, String descricao) {
            if (mensagemDeErro == null) {
                Endereco resultado = enderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L);
                Assertions.assertThat(resultado.getLogradouro()).isEqualTo("Rua das Flores");
                Assertions.assertThat(resultado.getBairro()).isEqualTo("Centro");
                Assertions.assertThat(resultado.getCep()).isEqualTo("12345-678");
                Assertions.assertThat(resultado.getCidade()).isEqualTo("São Paulo");
                Assertions.assertThat(resultado.getEstado()).isEqualTo("SP");
                consumidor.accept(resultado);
            } else {
                if (mensagemDeErro.contains("Endereço fora do padrão esperado: deve conter")) {
                    Assertions.assertThatThrownBy(() -> enderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining(mensagemDeErro);
                } else {
                    Assertions.assertThatThrownBy(() -> enderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L))
                            .isInstanceOf(BusinessException.class)
                            .hasMessage(mensagemDeErro);
                }
            }
        }

        public static Stream<Arguments> fornecerCenariosParaTesteDeveAnalisarEnderecoPlainTextEmDiversosCenarios() {

            Consumer<Endereco> consumidorAssertsEnderecoValido = endereco -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("123");
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("Apto 45");
            };

            Consumer<Endereco> consumidorAssertsEnderecoSemNumero = endereco -> {
                Assertions.assertThat(endereco.getNumero()).isNull();
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("Apto 45");
            };

            Consumer<Endereco> consumidorAssertsEnderecoSemComplemento = endereco -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("123");
                Assertions.assertThat(endereco.getComplemento()).isNull();
            };

            return Stream.of(
                    Arguments.of(buildEndereco(
                            "Rua das Flores, 123 - Apto 45",
                            "Centro",
                            "12345-678",
                            "São Paulo / SP"), null, consumidorAssertsEnderecoValido, "Endereço válido"),
                    Arguments.of(buildEndereco(
                            "Rua das Flores - Apto 45",
                            "Centro",
                            "12345-678",
                            "São Paulo / SP"), null, consumidorAssertsEnderecoSemNumero, "Endereço sem número"),
                    Arguments.of(buildEndereco(
                            "Rua das Flores, 123",
                            "Centro",
                            "12345-678",
                            "São Paulo / SP"), null, consumidorAssertsEnderecoSemComplemento, "Endereço sem complemento"),
                    Arguments.of(buildEndereco(
                                    "  ",
                                    "Centro",
                                    "12345-678",
                                    "São Paulo / SP"),
                            "Linha 1 do endereço inválida: deve conter o logradouro, número (opcional) e complemento (opcional).",
                            null, "Endereço sem número nem complemento inválido"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "12345-678",
                                    "São Paulo / SP"),
                            "Endereço fora do padrão esperado: deve conter",
                            null, "Endereço sem bairro fica com 1 linha a menos: bad-request"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "Centro",
                                    "12345-678"),
                            "Endereço fora do padrão esperado: deve conter",
                            null, "Endereço sem cidade e estado fica com 1 linha a menos: bad-request"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "Centro",
                                    "São Paulo / SP"),
                            "Endereço fora do padrão esperado: deve conter",
                            null, "Endereço sem CEP fica com 1 linha a menos: bad-request"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "Centro",
                                    "12-678",
                                    "São Paulo / SP"),
                            "CEP deve estar no formato 00000-000",
                            null, "Endereço com CEP inválido"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "Centro",
                                    "12345-678",
                                    "SP"),
                            "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.",
                            null, "Endereço sem cidade"),
                    Arguments.of(buildEndereco(
                                    "Rua das Flores, 123 - Apto 45",
                                    "Centro",
                                    "12345-678",
                                    "São Paulo - SP"),
                            "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.",
                            null, "Endereço com separador errado")
            );
        }

        private static String buildEndereco(String... partes) {
            return String.join(System.lineSeparator(), partes);
        }
    }
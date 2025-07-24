package com.example.api.clientes.unit.helper;

    import com.example.api.clientes.application.exception.BusinessException;
    import com.example.api.clientes.domain.model.Endereco;
    import com.example.api.clientes.helper.EnderecoParser;
    import com.example.api.clientes.helper.EnderecoPlainTextBuilder;
    import org.assertj.core.api.Assertions;
    import org.junit.jupiter.params.ParameterizedTest;
    import org.junit.jupiter.params.provider.Arguments;
    import org.junit.jupiter.params.provider.MethodSource;

    import java.util.function.Consumer;
    import java.util.stream.Stream;

    class EnderecoParserTest {

        private static final String SEPARADOR_MUNICIPIO_UF = "/";

        @ParameterizedTest(name = "{3}")
        @MethodSource("fornecerCenariosParaTesteDeveAnalisarEnderecoPlainTextEmDiversosCenarios")
        void deveAnalisarEnderecoPlainTextEmDiversosCenarios(String plainTextEndereco, String mensagemDeErro, Consumer<Endereco> consumidor, String descricao) {
            if (mensagemDeErro == null) {
                Endereco resultado = EnderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L, SEPARADOR_MUNICIPIO_UF);
                Assertions.assertThat(resultado.getLogradouro()).isEqualTo("Rua das Flores");
                Assertions.assertThat(resultado.getBairro()).isEqualTo("Centro");
                Assertions.assertThat(resultado.getCep()).isEqualTo("12345-678");
                Assertions.assertThat(resultado.getCidade()).isEqualTo("São Paulo");
                Assertions.assertThat(resultado.getEstado()).isEqualTo("SP");
            } else {
                if (mensagemDeErro.contains("Endereço fora do padrão esperado: deve conter")) {
                    Assertions.assertThatThrownBy(() -> EnderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L, SEPARADOR_MUNICIPIO_UF))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining(mensagemDeErro);
                } else {
                    Assertions.assertThatThrownBy(() -> EnderecoParser.getEnderecoFromPlainText(plainTextEndereco, 1L, SEPARADOR_MUNICIPIO_UF))
                            .isInstanceOf(BusinessException.class)
                            .hasMessage(mensagemDeErro);
                }
            }
        }

        public static Stream<Arguments> fornecerCenariosParaTesteDeveAnalisarEnderecoPlainTextEmDiversosCenarios() {

            Consumer<Endereco> consumidorAssertsEnderecoValido = (endereco) -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("123");
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("Apto 45");
            };

            Consumer<Endereco> consumidorAssertsEnderecoSemNumero = (endereco) -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("S/N");
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("Apto 45");
            };

            Consumer<Endereco> consumidorAssertsEnderecoSemComplemento = (endereco) -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("123");
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("");
            };

            Consumer<Endereco> consumidorAssertsEnderecoComSnESemComplemento = (endereco) -> {
                Assertions.assertThat(endereco.getNumero()).isEqualTo("S/N");
                Assertions.assertThat(endereco.getComplemento()).isEqualTo("");
            };

            return Stream.of(
                    Arguments.of(EnderecoPlainTextBuilder.build(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoValido, "Endereço válido"),
                    Arguments.of(EnderecoPlainTextBuilder.semNumero(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoSemNumero, "Endereço sem número"),
                    Arguments.of(EnderecoPlainTextBuilder.semComplemento(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoSemComplemento, "Endereço sem complemento"),
                    Arguments.of(EnderecoPlainTextBuilder.comSnESemComplemento(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoComSnESemComplemento, "Endereço com S/N mas sem complemento"),
                    Arguments.of(EnderecoPlainTextBuilder.comSnEComplemento(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoSemNumero, "Endereço com S/N e complemento"),
                    Arguments.of(EnderecoPlainTextBuilder.soLogradouroValido(SEPARADOR_MUNICIPIO_UF), null, consumidorAssertsEnderecoComSnESemComplemento, "Endereço sem número nem complemento válido"),
                    Arguments.of(EnderecoPlainTextBuilder.soLogradouroInvalido(SEPARADOR_MUNICIPIO_UF), "Linha 1 do endereço inválida: deve conter o logradouro, número e complemento.", null, "Endereço sem número nem complemento inválido"),
                    Arguments.of(EnderecoPlainTextBuilder.semBairro(SEPARADOR_MUNICIPIO_UF), "Endereço fora do padrão esperado: deve conter", null, "Endereço sem bairro fica com 1 linha a menos: bad-request"),
                    Arguments.of(EnderecoPlainTextBuilder.semCidadeEstado(), "Endereço fora do padrão esperado: deve conter", null, "Endereço sem cidade e estado fica com 1 linha a menos: bad-request"),
                    Arguments.of(EnderecoPlainTextBuilder.semCep(SEPARADOR_MUNICIPIO_UF), "Endereço fora do padrão esperado: deve conter", null, "Endereço sem CEP fica com 1 linha a menos: bad-request"),
                    Arguments.of(EnderecoPlainTextBuilder.comCep("345-678", SEPARADOR_MUNICIPIO_UF), "CEP deve estar no formato 00000-000", null, "Endereço com CEP inválido"),
                    Arguments.of(EnderecoPlainTextBuilder.semCidade(), "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.", null, "Endereço sem cidade"),
                    Arguments.of(EnderecoPlainTextBuilder.buildComSeparadorErrado(SEPARADOR_MUNICIPIO_UF), "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.", null, "Endereço com separador errado")
            );
        }
    }
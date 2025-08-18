package com.example.api.clientes.helper;

import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.domain.model.Endereco;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnderecoParser {

    private static final int NUM_LINHAS_ENDERECO = 4;
    private static final Pattern PRIMEIRA_LINHA_ENDERECO_PATTERN = Pattern.compile("^(?<logradouro>[^,\\-]+?)(?:,\\s*(?<numero>\\d+))?(?:\\s*-\\s*(?<comp>.+))?$");
    private final Pattern linhaCidadeEstadoPattern;

    public EnderecoParser(String separadorMunicipioUf) {
        this.linhaCidadeEstadoPattern = Pattern.compile("(?<cidade>(?:\\p{L}|-|\\s)+)\\s*\\" + separadorMunicipioUf + "\\s*([a-zA-Z]{2})");
    }

    public Endereco getEnderecoFromPlainText(String plainTextEndereco, Long idPessoa) {
        String[] linhas = getLinhas(plainTextEndereco);
        String linhaLogradouro = linhas[0].trim();
        String linhaBairro = linhas[1].trim();
        String linhaCep = linhas[2].trim();
        String linhaCidadeEstado = linhas[3].trim();

        String[] elementosLinha1 = extrairElementosLinha1(linhaLogradouro);
        String logradouro = elementosLinha1[0];
        String numero = elementosLinha1[1];
        String complemento = elementosLinha1[2];

        String bairro = linhaBairro.trim();
        validateLine(!bairro.isEmpty(), "Linha 2 do endereço inválida: deve conter o bairro.");

        validateLine(linhaCep.matches("\\d{5}-?\\d{3}"), "CEP deve estar no formato 00000-000");
        String cep = linhaCep.trim();

        Matcher linhaCidadeEstadoMatcher = linhaCidadeEstadoPattern.matcher(linhaCidadeEstado);
        validateLine(linhaCidadeEstadoMatcher.find(), "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.");
        String cidade = linhaCidadeEstadoMatcher.group(1).trim();
        String estado = linhaCidadeEstadoMatcher.group(2).trim();

        return new Endereco(idPessoa, logradouro, numero, complemento, bairro, cep, cidade, estado);
    }

    private static String[] extrairElementosLinha1(String linha1) {
        String logradouro;
        String complemento;
        String numero;

        Matcher primeiraLinhaMatcher = PRIMEIRA_LINHA_ENDERECO_PATTERN.matcher(linha1);
        validateLine(primeiraLinhaMatcher.find(), "Linha 1 do endereço inválida: deve conter o logradouro, número (opcional) e complemento (opcional).");

        logradouro = primeiraLinhaMatcher.group("logradouro").trim();
        numero = primeiraLinhaMatcher.group("numero");
        complemento = primeiraLinhaMatcher.group("comp") != null ? primeiraLinhaMatcher.group("comp").trim() : null;

        return new String[]{logradouro, numero, complemento};
    }

    private static void validateLine(boolean condition, String errorMessage) {
        if (!condition) {
            throw new BusinessException(errorMessage);
        }
    }

    private static String[] getLinhas(String plainTextEndereco) {
        String[] lines = plainTextEndereco.split(System.lineSeparator());
        if (lines.length != NUM_LINHAS_ENDERECO) {
            throw new IllegalArgumentException(String.format("Endereço fora do padrão esperado: deve conter %s linhas.", NUM_LINHAS_ENDERECO));
        }
        return lines;
    }
}
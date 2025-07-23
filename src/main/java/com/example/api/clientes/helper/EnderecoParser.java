package com.example.api.clientes.helper;

import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.domain.model.Endereco;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnderecoParser {

    private static final int NUM_LINHAS_ENDERECO = 4;
    private static final Pattern LOGRADOURO_NUMERO_COMPLEMENTO_PATTERN = Pattern.compile("(.+),\\s*(\\S+)\\s*-\\s*(.+)");
    private static final Pattern LOGRADOURO_NUMERO_PATTERN = Pattern.compile("(.+),\\s*(\\S+)");
    private static final Pattern LOGRADOURO_COMPLEMENTO_PATTERN = Pattern.compile("(.+)-\\s*(.+)");
    private static final Pattern LOGRADOURO_SEM_NUMERO_SEM_COMP_PATTERN = Pattern.compile("(.+)");

    public static Endereco getEnderecoFromPlainText(String plainTextEndereco, Long idPessoa, String separadorMunicipioUf) {
        String[] linhas = getLinhas(plainTextEndereco);
        String linha1 = linhas[0].trim();
        String linha2 = linhas[1].trim();
        String linha3 = linhas[2].trim();
        String linha4 = linhas[3].trim();

        String[] elementosLinha1 = extrairElementosLinha1(linha1);
        String logradouro = elementosLinha1[0].trim();
        String numero = elementosLinha1[1].trim();
        String complemento = elementosLinha1[2].trim();

        String bairro = linha2.trim();
        validateLine(!bairro.isEmpty(), "Linha 2 do endereço inválida: deve conter o bairro.");

        validateLine(linha3.matches("\\d{5}-?\\d{3}"), "CEP deve estar no formato 00000-000");
        String cep = linha3.trim();

        Matcher linha4Matcher = Pattern.compile("(.*)" + separadorMunicipioUf + "(.*)").matcher(linha4);
        validateLine(linha4Matcher.find(), "Linha 4 do endereço inválida: deve conter '<cidade> / <estado>'.");
        String cidade = linha4Matcher.group(1).trim();
        String estado = linha4Matcher.group(2).trim();

        return new Endereco(idPessoa, logradouro, numero, complemento, bairro, cep, cidade, estado);
    }

    private static String[] extrairElementosLinha1(String linha1) {
        String logradouro;
        String complemento;
        String numero;


        if (LOGRADOURO_NUMERO_COMPLEMENTO_PATTERN.matcher(linha1).matches()) {
            Matcher linha1Matcher = LOGRADOURO_NUMERO_COMPLEMENTO_PATTERN.matcher(linha1);
            validateLine(linha1Matcher.find(), "O formato da Linha 1 do endereço está inválido. Deve ser '<logradouro>, <numero> - <complemento>' ou '<logradouro>, S/N - <complemento>'.");
            logradouro = linha1Matcher.group(1);
            numero = linha1Matcher.group(2);
            complemento = linha1Matcher.group(3).trim();
        } else if (LOGRADOURO_NUMERO_PATTERN.matcher(linha1).matches()) {
            Matcher linha1Matcher = LOGRADOURO_NUMERO_PATTERN.matcher(linha1);
            validateLine(linha1Matcher.find(), "O formato da Linha 1 do endereço está inválido. Deve ser '<logradouro>, <numero> - <complemento>' ou '<logradouro>, S/N - <complemento>'.");
            logradouro = linha1Matcher.group(1);
            numero = linha1Matcher.group(2);
            complemento = "";
        } else if (LOGRADOURO_COMPLEMENTO_PATTERN.matcher(linha1).matches()) {
            Matcher linha1Matcher = LOGRADOURO_COMPLEMENTO_PATTERN.matcher(linha1);
            validateLine(linha1Matcher.find(), "O formato da Linha 1 do endereço está inválido. Deve ser '<logradouro>, <numero> - <complemento>' ou '<logradouro>, S/N - <complemento>'.");
            logradouro = linha1Matcher.group(1);
            numero = "S/N";
            complemento = linha1Matcher.group(2).trim();
        } else if (LOGRADOURO_SEM_NUMERO_SEM_COMP_PATTERN.matcher(linha1).matches()) {
            Matcher linha1Matcher = LOGRADOURO_SEM_NUMERO_SEM_COMP_PATTERN.matcher(linha1);
            validateLine(linha1Matcher.find(), "O formato da Linha 1 do endereço está inválido. Deve ser '<logradouro>, <numero> - <complemento>' ou '<logradouro>, S/N - <complemento>'.");
            logradouro = linha1Matcher.group(1);
            numero = "S/N";
            complemento = "";
        }
        else {
            throw new BusinessException("Linha 1 do endereço inválida: deve conter o logradouro, número e complemento.");
        }
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
            throw new BusinessException(String.format("Endereço fora do padrão esperado: deve conter %s linhas.", NUM_LINHAS_ENDERECO));
        }
        return lines;
    }
}
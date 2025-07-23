package com.example.api.clientes.helper;

public class EnderecoPlainTextBuilder {

    private static final String logradouro = "Rua das Flores";
    private static final String numero = "123";
    private static final String complemento = "Apto 45";
    private static final String bairro = "Centro";
    private static final String cep = "12345-678";
    private static final String cidade = "São Paulo";
    private static final String estado = "SP";

    public static String semNumeroNemComplemento(String separador){
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), logradouro, bairro, cep, linha4);
    }

    public static String semNumero(String separador){
        String linha1 = String.format("%s - %s", logradouro, complemento);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String semComplemento(String separador){
        String linha1 = String.format("%s, %s", logradouro, numero);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String comSnESemComplemento(String separador){
        String linha1 = String.format("%s, S/N", logradouro);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String comSnEComplemento(String separador){
        String linha1 = String.format("%s, S/N - %s", logradouro, complemento);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String semBairro(String separador){
        String linha1 = String.format("%s, %s - %s", logradouro, numero, complemento);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, cep, linha4);
    }

    public static String semCidadeEstado() {
        String linha1 = String.format("%s, %s - %s", logradouro, numero, complemento);
        return String.join(System.lineSeparator(), linha1, bairro, cep);
    }

    public static String semCep(String separador) {
        String linha1 = String.format("%s, %s - %s", logradouro, numero, complemento);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, linha4);
    }

    public static String buildComSeparadorErrado(String separador) {
        String logradouroNumeroComplemento = String.format("%s-%s-%s", logradouro, numero, complemento);
        String linha4;
        if (separador.equals("/")) linha4 = String.format("%s%s%s", cidade, "-", estado);
        else linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), logradouroNumeroComplemento, bairro, cep, linha4);
    }

    public static String buildComSeparadorSlash(String separador) {
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        String logradouroNumeroComplemento = String.format("%s, %s - %s", logradouro, numero, complemento);
        return String.join(System.lineSeparator(), logradouroNumeroComplemento, bairro, cep, linha4);
    }

    public static String comCep (String cep, String separador) {
        String linha1 = String.format("%s, %s - %s", logradouro, numero, complemento);
        String linha4 = String.format("%s%s%s", cidade, separador, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String semCidade() {
        String linha1 = String.format("%s, %s - %s", logradouro, numero, complemento);
        return String.join(System.lineSeparator(), linha1, bairro, cep, estado);
    }

    public static String build(String separador){
        return String.join(System.lineSeparator(),
                String.format("%s, %s - %s", logradouro, numero, complemento),
                bairro,
                cep,
                String.format("%s%s%s", cidade, separador, estado));
    }

    public static String soLogradouroValido(String separadorMunicipioUf) {
        String linha1 = String.format("%s", logradouro);
        String linha4 = String.format("%s%s%s", cidade, separadorMunicipioUf, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }

    public static String soLogradouroInvalido(String separadorMunicipioUf) {
        String linha1 = " ";
        String linha4 = String.format("%s%s%s", cidade, separadorMunicipioUf, estado);
        return String.join(System.lineSeparator(), linha1, bairro, cep, linha4);
    }
}
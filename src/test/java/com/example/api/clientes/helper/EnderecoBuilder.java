package com.example.api.clientes.helper;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.domain.model.Endereco;

public class EnderecoBuilder {

    private Long id = 1L;
    private String logradouro = "Rua das Flores";
    private Integer numero = 123;
    private String complemento = "Apto 45";
    private String bairro = "Centro";
    private String cep = "12345-678";
    private String cidade = "São Paulo";
    private String estado = "SP";

    public EnderecoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EnderecoBuilder withLogradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public EnderecoBuilder withNumero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public EnderecoBuilder withComplemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public EnderecoBuilder withBairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public EnderecoBuilder withCep(String cep) {
        this.cep = cep;
        return this;
    }

    public EnderecoBuilder withCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public EnderecoBuilder withEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public Endereco build() {
        return new Endereco(id, logradouro, numero, complemento, bairro, cep, cidade, estado);
    }

    public EnderecoRequest buidRequest() {
        return new EnderecoRequest(logradouro, numero, complemento, bairro, cep, cidade, estado);
    }

    public EnderecoResponse buildResponse() {
        return new EnderecoResponse(id, logradouro, numero, complemento, bairro, cep, cidade, estado);
    }
}

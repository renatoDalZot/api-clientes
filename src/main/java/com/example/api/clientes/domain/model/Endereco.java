package com.example.api.clientes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "endereco")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endereco {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "logradouro")
    @NotEmpty(message = "Logradouro não pode ser vazio")
    private String logradouro;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "complemento")
    private String complemento;
    @NotEmpty(message = "Bairro não pode ser vazio")
    @Column(name = "bairro")
    private String bairro;
    @NotEmpty(message = "CEP não pode ser vazio")
    @Column(name = "cep")
    private String cep;
    @NotEmpty(message = "Cidade não pode ser vazio")
    @Column(name = "cidade")
    private String cidade;
    @NotEmpty(message = "Estado não pode ser vazio")
    @Column(name = "estado")
    private String estado;

    public Endereco(Long pessoaFisicaId, String logradouro, int numero, String complemento, String bairro, String cep, String cidade, String estado) {
        this.id = pessoaFisicaId;
        validarCep(cep);
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
    }

    private void validarCep(String cep) {
        if (!cep.matches("\\d{5}-\\d{3}")) throw new IllegalArgumentException("CEP deve estar no formato 00000-000");
    }
}
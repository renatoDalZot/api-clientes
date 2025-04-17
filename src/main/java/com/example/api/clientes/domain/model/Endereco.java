package com.example.api.clientes.domain.model;

import com.example.api.clientes.application.dto.EnderecoResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "endereco")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endereco {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "simulacao_seq_generator")
    @SequenceGenerator(name = "simulacao_seq_generator", sequenceName = "endereco_seq", allocationSize = 1)
    private Long id;
    @OneToOne(mappedBy = "endereco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotEmpty(message = "Pessoa Física não pode ser vazia")
    private Long pessoaFisicaId;
    @NotEmpty(message = "Logradouro não pode ser vazio")
    @Column(name = "logradouro")
    private String logradouro;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "bairro")
    private String bairro;
    @Column(name = "cep")
    @NotEmpty(message = "CEP não pode ser vazio")
    private String cep;
    @Column(name = "municipio")
    @NotEmpty(message = "Município não pode ser vazio")
    private String municipio;
    @NotEmpty(message = "UF não pode ser vazio")
    @Column(name = "uf")
    private String uf;

    public Endereco(Long pessoaFisicaId, String logradouro, Integer numero, String complemento, String bairro,
                    String cep, String municipio, String uf) {
        this.pessoaFisicaId = pessoaFisicaId;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.municipio = municipio;
        this.uf = uf;
    }

    public EnderecoResponse toResponse() {
        return new EnderecoResponse(this.id, this.pessoaFisicaId, this.logradouro, this.numero, this.complemento,
                this.bairro, this.cep, this.municipio, this.uf);
    }
}

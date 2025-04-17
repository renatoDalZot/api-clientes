package com.example.api.clientes.domain.model;

import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Table(name = "pessoa_fisica")
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PessoaFisica {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "simulacao_seq_generator")
    @SequenceGenerator(name = "simulacao_seq_generator", sequenceName = "pessoa_fisica_seq", allocationSize = 1)
    private Long id;
    @NotEmpty(message = "Nome não pode ser vazio")
    @Column(name = "nome")
    private String nome;
    @NotEmpty(message = "CPF não pode ser vazio")
    @Column (name = "cpf", unique = true)
    private String cpf;
    @NotNull(message = "Data de nascimento não pode ser nula")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    @NotNull(message = "Data de cadastro não pode ser nula")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    @Column(name = "renda_mensal")
    private BigDecimal rendaMensal;
    @Column(name = "score")
    private Double score;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;


    public PessoaFisica(String nome, String cpf, LocalDate dataCadastro, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataCadastro = dataCadastro;
        this.dataNascimento = dataNascimento;
        this.score = calcularScore();
        if (!isMaiorDeIdade()) throw new IllegalArgumentException("Idade mínima para esta operação é 18 anos.");
    }

    private Double calcularScore() {
        double score = 5;
        int idade = Period.between(this.dataNascimento, this.dataCadastro).getYears();
        score += (idade < 50) ? (int)((idade - 18)/5) : 5 - (int)((idade - 50)/5) * 0.5;
        if (score < 5) score = 5;
        return score;
    }

    private boolean isMaiorDeIdade() {
        Period period = Period.between(this.dataNascimento, this.dataCadastro);
        int idade = period.getYears();
        return idade >= 18;
    }

    public PessoaFisicaResponse toResponse() {
        return new PessoaFisicaResponse(this.id, this.nome, this.cpf, this.dataCadastro, this.dataNascimento,
                this.rendaMensal, this.score);
    }

    public void setDatas (LocalDate dataCadastro, LocalDate dataNascimento) {
        if (Period.between(dataNascimento, dataCadastro).getYears() < 18) throw new
                IllegalArgumentException("Idade mínima para esta operação é 18 anos.");
        this.dataCadastro = dataCadastro;
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        if (Period.between(dataNascimento, this.dataCadastro).getYears() < 18) throw new
                IllegalArgumentException("Idade mínima para esta operação é 18 anos.");
        this.dataNascimento = dataNascimento;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        if (dataCadastro.isBefore(this.dataNascimento)) throw new
                IllegalArgumentException("Data de cadastro não pode ser anterior a data de nascimento.");
        if (Period.between(this.dataNascimento, dataCadastro).getYears() < 18) throw new
                IllegalArgumentException("Idade mínima para esta operação é 18 anos.");
        this.dataCadastro = dataCadastro;
    }
}

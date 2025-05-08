package com.example.api.clientes.domain.model;

import com.example.api.clientes.application.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Table(name = "pessoa_fisica")
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PessoaFisica {

    private static final int IDADE_MINIMA = 18;
    private static final int IDADE_DECLINIO = 50;
    private static final int SCORE_INICIAL = 5;
    private static final int SCORE_MAXIMO = 10;
    private static final int CICLO_QUINQUENAL = 5;
    private static final int INCREMENTO = 1;
    private static final double DECREMENTO = 0.5;

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


    public PessoaFisica(String nome, String cpf, LocalDate dataCadastro, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataCadastro = dataCadastro;
        this.dataNascimento = dataNascimento;
        this.score = calcularScore();
        if (!isMaiorDeIdade()) throw new BusinessException("Idade mínima para esta operação é 18 anos.");
    }

    private Double calcularScore() {
        int idade = Period.between(this.dataNascimento, this.dataCadastro).getYears();
        double score = (idade < IDADE_DECLINIO) ?
                SCORE_INICIAL + Math.floorDiv(idade - IDADE_MINIMA, CICLO_QUINQUENAL) * INCREMENTO :
                SCORE_MAXIMO - Math.floorDiv(idade - IDADE_DECLINIO, CICLO_QUINQUENAL) * DECREMENTO;
        if (score < SCORE_INICIAL) score = SCORE_INICIAL;
        if (score > SCORE_MAXIMO) score = SCORE_MAXIMO;
        return score;
    }

    private boolean isMaiorDeIdade() {
        Period period = Period.between(this.dataNascimento, this.dataCadastro);
        int idade = period.getYears();
        return idade >= 18;
    }
}
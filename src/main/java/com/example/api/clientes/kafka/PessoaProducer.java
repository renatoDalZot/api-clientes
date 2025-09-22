package com.example.api.clientes.kafka;

import com.example.api.clientes.application.dto.KafkaError;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.api.clientes.application.dto.PessoaFisicaResponse;

@Component
@Slf4j
public class PessoaProducer {

    private final String TOPICO_PESSOA_CADASTRADA = "pessoa-cadastrada";
    private final String TOPICO_PESSOA_CADASTRADA_ERRO = "pessoa-cadastrada-erro";
    private final KafkaTemplate<String, PessoaFisicaResponse> kafkaTemplate;
    private final KafkaTemplate<String, KafkaError> kafkaErrorTemplate;

    public PessoaProducer(KafkaTemplate<String, PessoaFisicaResponse> kafkaTemplate, KafkaTemplate<String, KafkaError> kafkaErrorTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaErrorTemplate = kafkaErrorTemplate;
    }

    public void enviarPessoaCadastrada(PessoaFisicaResponse pessoa) {
        kafkaTemplate.send(TOPICO_PESSOA_CADASTRADA, pessoa);
    }

    public void enviarPessoaCadastradaErro(KafkaError mensagem) {
        kafkaErrorTemplate.send(TOPICO_PESSOA_CADASTRADA_ERRO, mensagem);
    }
}

package com.example.api.clientes.kafka;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.service.PessoaFisicaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PessoaConsumer {

    private static final String TOPICO_CRIAR_PESSOA_FISICA = "criar-pessoa-fisica";

    private final PessoaFisicaService pessoaFisicaService;
    private final PessoaProducer pessoaProducer;

    public PessoaConsumer(PessoaFisicaService pessoaFisicaService, PessoaProducer pessoaProducer) {
        this.pessoaFisicaService = pessoaFisicaService;
        this.pessoaProducer = pessoaProducer;
    }

    @KafkaListener(id = "validated", topics = TOPICO_CRIAR_PESSOA_FISICA, groupId = "pessoa-group")
    public void consumirCadastroPessoaFisica(@Valid @Payload PessoaFisicaRequest  mensagem) {
        log.info("Mensagem de cadastro de pessoa física recebida: " + mensagem);
        var pessoaSalva = pessoaFisicaService.cadastrar(mensagem);
        log.info("Pessoa física cadastrada com ID: " + pessoaSalva.id());
        pessoaProducer.enviarPessoaCadastrada(pessoaSalva);
    }
}

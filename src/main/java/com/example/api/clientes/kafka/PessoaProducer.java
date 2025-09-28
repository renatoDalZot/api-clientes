package com.example.api.clientes.kafka;

import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PessoaProducer {

    private static final String TOPICO_PESSOA_CADASTRADA = "pessoa-cadastrada";
    private final KafkaTemplate<String, PessoaFisicaResponse> kafkaTemplate;

    public PessoaProducer(KafkaTemplate<String, PessoaFisicaResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarPessoaCadastrada(PessoaFisicaResponse pessoa) {
        kafkaTemplate.send(TOPICO_PESSOA_CADASTRADA, pessoa);
    }
}

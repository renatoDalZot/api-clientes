package com.example.api.clientes.kafka;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsoleLogConsumer {

//    @KafkaListener(topics = "criar-pessoa-fisica", groupId = "console-log-grup")
    public void logarMensagem(PessoaFisicaRequest pessoaFisicaRequest) {
        log.info("ConsoleLogConsumer recebeu: {}", pessoaFisicaRequest);
    }
}

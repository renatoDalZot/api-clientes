package com.example.api.clientes.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsoleLogConsumer {

    @KafkaListener(topics = "criar-pessoa-fisica", groupId = "console-log-grup", containerFactory = "kafkaListenerStringContainerFactory")
    public void logarMensagem(String pessoaFisicaRequest) {
        log.info("ConsoleLogConsumer recebeu: {}", pessoaFisicaRequest);
    }
}

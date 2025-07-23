package com.example.api.clientes.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PessoaConsumer {

    @KafkaListener(topics = "criar-pessoa-fisica", groupId = "pessoa-group")
        public void consumirMensagem(String mensagem) {
        // Lógica para processar a mensagem recebida do Kafka
        log.info("Mensagem recebida: " + mensagem);

        // Aqui você pode adicionar lógica para processar a mensagem, como salvar no banco de dados
        // ou realizar outras operações necessárias.
    }
}

//package com.example.api.clientes.integration.kafka;
//
//import com.example.api.clientes.application.dto.PessoaFisicaRequest;
//import com.example.api.clientes.application.dto.PessoaFisicaResponse;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.
//import org.springframework.kafka.test.consumer.TestConsumer;
//import org.springframework.test.annotation.DirtiesContext;
//
//import java.time.LocalDate;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@EmbeddedKafka(partitions = 1, topics = {"criar-pessoa-fisica", "pessoa-cadastrada"})
//@DirtiesContext
//@EnableKafka
//public class PessoaKafkaIntegrationTest {
//
//    @Autowired
//    private KafkaTemplate<String, PessoaFisicaRequest> requestKafkaTemplate;
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//    @Test
//    void testConsumerAndProducerFlow() {
//        PessoaFisicaRequest request = new PessoaFisicaRequest("João Silva", "12345678900", LocalDate.of(2000,1,1));
//
//        requestKafkaTemplate.send("criar-pessoa-fisica", request);
//
//        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
//                "testGroup", "true", embeddedKafkaBroker);
//        TestConsumer<String, PessoaFisicaResponse> consumer = new TestConsumer<>(
//                consumerProps, new StringDeserializer(), /* your PessoaFisicaResponse deserializer */);
//
//        consumer.subscribe("pessoa-cadastrada");
//        ConsumerRecord<String, PessoaFisicaResponse> record = consumer.poll(10000).iterator().next();
//
//        assertThat(record.value()).isNotNull();
//        // Add more assertions as needed
//    }
//}
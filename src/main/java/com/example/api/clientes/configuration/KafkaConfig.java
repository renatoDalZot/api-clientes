package com.example.api.clientes.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashMap;

@Configuration
@Slf4j
public class KafkaConfig implements KafkaListenerConfigurer {

    private final LocalValidatorFactoryBean validator;
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(LocalValidatorFactoryBean validator, KafkaProperties kafkaProperties) {
        this.validator = validator;
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, (r, e) -> {
            log.error("Sending to DLT", e);
            return new TopicPartition(r.topic() + ".DLT", r.partition());
        });
        return new DefaultErrorHandler(recoverer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory(
            DefaultErrorHandler errorHandler) {

        var configs = new HashMap<String, Object>(kafkaProperties.buildConsumerProperties());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(configs);
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }
}

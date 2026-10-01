package com.enterprisehub.backend.product.infrastructure.messaging;

import com.enterprisehub.backend.product.application.ProductEventPublisher;
import com.enterprisehub.backend.product.domain.StockLowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "enterprisehub.messaging.kafka.enabled", havingValue = "true")
public class KafkaProductEventPublisher implements ProductEventPublisher {
    private final KafkaTemplate<String, StockLowEvent> kafkaTemplate;

    @Value("${enterprisehub.messaging.kafka.product-events-topic}")
    private String topic;

    @Override
    public void publish(StockLowEvent event) {
        kafkaTemplate.send(topic, event.productId().toString(), event);
    }
}

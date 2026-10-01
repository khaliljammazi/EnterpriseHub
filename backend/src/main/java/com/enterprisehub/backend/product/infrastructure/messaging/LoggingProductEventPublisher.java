package com.enterprisehub.backend.product.infrastructure.messaging;

import com.enterprisehub.backend.product.application.ProductEventPublisher;
import com.enterprisehub.backend.product.domain.StockLowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "enterprisehub.messaging.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingProductEventPublisher implements ProductEventPublisher {
    @Override
    public void publish(StockLowEvent event) {
        log.info("Kafka disabled - low stock event not sent: {}", event);
    }
}

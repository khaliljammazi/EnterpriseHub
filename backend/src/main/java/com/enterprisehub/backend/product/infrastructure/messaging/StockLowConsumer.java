package com.enterprisehub.backend.product.infrastructure.messaging;

import com.enterprisehub.backend.product.domain.StockLowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "enterprisehub.messaging.kafka.enabled", havingValue = "true")
public class StockLowConsumer {
    @KafkaListener(
            topics = "${enterprisehub.messaging.kafka.product-events-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(StockLowEvent event) {
        log.warn("Low stock notification: companyId={}, productId={}, sku={}, remaining={}",
                event.companyId(), event.productId(), event.sku(), event.remainingQuantity());
    }
}

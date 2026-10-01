package com.enterprisehub.backend.company.infrastructure.messaging;

import com.enterprisehub.backend.company.domain.CompanyCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "enterprisehub.messaging.kafka.enabled", havingValue = "true")
public class CompanyCreatedConsumer {

    @KafkaListener(
            topics = "${enterprisehub.messaging.kafka.company-events-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CompanyCreatedEvent event) {
        log.info(
                "Notification: company created id={}, name={}, type={}",
                event.companyId(),
                event.companyName(),
                event.companyType()
        );
    }
}

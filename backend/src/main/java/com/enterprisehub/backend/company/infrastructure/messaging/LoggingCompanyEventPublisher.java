package com.enterprisehub.backend.company.infrastructure.messaging;

import com.enterprisehub.backend.company.application.CompanyEventPublisher;
import com.enterprisehub.backend.company.domain.CompanyCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "enterprisehub.messaging.kafka.enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class LoggingCompanyEventPublisher implements CompanyEventPublisher {

    @Override
    public void publish(CompanyCreatedEvent event) {
        log.info("Kafka disabled - company event not sent: {}", event);
    }
}

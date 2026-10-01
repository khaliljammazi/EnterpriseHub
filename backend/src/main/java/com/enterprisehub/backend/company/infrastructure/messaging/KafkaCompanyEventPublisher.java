package com.enterprisehub.backend.company.infrastructure.messaging;

import com.enterprisehub.backend.company.application.CompanyEventPublisher;
import com.enterprisehub.backend.company.domain.CompanyCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "enterprisehub.messaging.kafka.enabled", havingValue = "true")
public class KafkaCompanyEventPublisher implements CompanyEventPublisher {

    private final KafkaTemplate<String, CompanyCreatedEvent> kafkaTemplate;

    @Value("${enterprisehub.messaging.kafka.company-events-topic}")
    private String topic;

    @Override
    public void publish(CompanyCreatedEvent event) {
        kafkaTemplate.send(topic, event.companyId().toString(), event);
    }
}

package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.company.domain.CompanyCreatedEvent;

public interface CompanyEventPublisher {

    void publish(CompanyCreatedEvent event);
}

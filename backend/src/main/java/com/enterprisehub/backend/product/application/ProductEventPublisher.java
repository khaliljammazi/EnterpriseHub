package com.enterprisehub.backend.product.application;

import com.enterprisehub.backend.product.domain.StockLowEvent;

public interface ProductEventPublisher {
    void publish(StockLowEvent event);
}

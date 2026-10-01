package com.enterprisehub.backend.product.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyName;
import com.enterprisehub.backend.company.domain.CompanyRepository;
import com.enterprisehub.backend.company.domain.CompanyType;
import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.PasswordHash;
import com.enterprisehub.backend.identity.domain.Role;
import com.enterprisehub.backend.identity.domain.User;
import com.enterprisehub.backend.identity.domain.UserRepository;
import com.enterprisehub.backend.product.domain.Product;
import com.enterprisehub.backend.product.domain.ProductRepository;
import com.enterprisehub.backend.product.domain.StockLowEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock ProductRepository productRepository;
    @Mock CompanyRepository companyRepository;
    @Mock UserRepository userRepository;
    @Mock ProductEventPublisher productEventPublisher;
    @InjectMocks ProductService productService;

    @Test
    void createsProductForCompanyAndAuthenticatedUser() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Company company = Company.rehydrate(companyId, CompanyName.of("Shop"), CompanyType.GROCERY_STORE, Instant.now());
        User user = User.rehydrate(userId, Email.of("owner@example.com"), new PasswordHash("hash"),
                Set.of(Role.MANAGER), true, Instant.now());
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(Email.of("owner@example.com"))).thenReturn(Optional.of(user));
        when(productRepository.existsByCompanyIdAndSku(companyId, "COFFEE-01")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return Product.rehydrate(UUID.randomUUID(), product.companyId(), product.createdByUserId(),
                    product.sku(), product.name(), product.price(), product.stockQuantity(), product.createdAt());
        });

        ProductResult result = productService.create(companyId, "owner@example.com",
                new CreateProductCommand(" coffee-01 ", "Coffee", new BigDecimal("4.50"), 20));

        assertThat(result.companyId()).isEqualTo(companyId);
        assertThat(result.createdByUserId()).isEqualTo(userId);
        assertThat(result.sku()).isEqualTo("COFFEE-01");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void rejectsDuplicateSkuInsideCompany() {
        UUID companyId = UUID.randomUUID();
        Company company = Company.rehydrate(companyId, CompanyName.of("Shop"), CompanyType.GROCERY_STORE, Instant.now());
        User user = User.rehydrate(UUID.randomUUID(), Email.of("owner@example.com"), new PasswordHash("hash"),
                Set.of(Role.MANAGER), true, Instant.now());
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(Email.of("owner@example.com"))).thenReturn(Optional.of(user));
        when(productRepository.existsByCompanyIdAndSku(companyId, "SKU-1")).thenReturn(true);

        assertThatThrownBy(() -> productService.create(companyId, "owner@example.com",
                new CreateProductCommand("sku-1", "Coffee", BigDecimal.ONE, 1)))
                .isInstanceOf(ConflictException.class)
                .hasMessage("A product with this SKU already exists in the company");
    }

    @Test
    void publishesEventWhenStockBecomesLow() {
        UUID companyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = Product.rehydrate(productId, companyId, UUID.randomUUID(), "SKU-1", "Coffee",
                BigDecimal.ONE, 10, Instant.now());
        when(productRepository.findByIdAndCompanyId(productId, companyId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.update(companyId, productId,
                new UpdateProductCommand("Coffee", BigDecimal.ONE, 5));

        verify(productEventPublisher).publish(any(StockLowEvent.class));
    }
}

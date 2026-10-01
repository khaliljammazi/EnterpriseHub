package com.enterprisehub.backend.product.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.common.NotFoundException;
import com.enterprisehub.backend.company.domain.CompanyRepository;
import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.User;
import com.enterprisehub.backend.identity.domain.UserRepository;
import com.enterprisehub.backend.product.domain.Product;
import com.enterprisehub.backend.product.domain.ProductRepository;
import com.enterprisehub.backend.product.domain.StockLowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ProductEventPublisher productEventPublisher;

    @Transactional
    public ProductResult create(UUID companyId, String creatorEmail, CreateProductCommand command) {
        requireCompany(companyId);
        User creator = userRepository.findByEmail(Email.of(creatorEmail))
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
        String normalizedSku = command.sku() == null ? null : command.sku().trim().toUpperCase();
        if (normalizedSku != null && productRepository.existsByCompanyIdAndSku(companyId, normalizedSku)) {
            throw new ConflictException("A product with this SKU already exists in the company");
        }
        Product product = Product.create(companyId, creator.id(), command.sku(), command.name(),
                command.price(), command.stockQuantity());
        Product savedProduct = productRepository.save(product);
        if (savedProduct.isLowStock()) {
            productEventPublisher.publish(StockLowEvent.from(savedProduct));
        }
        return ProductResult.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResult> findAll(UUID companyId) {
        requireCompany(companyId);
        return productRepository.findAllByCompanyId(companyId).stream().map(ProductResult::from).toList();
    }

    @Transactional
    public ProductResult update(UUID companyId, UUID productId, UpdateProductCommand command) {
        Product product = findProduct(companyId, productId);
        boolean becameLowStock = product.update(command.name(), command.price(), command.stockQuantity());
        Product savedProduct = productRepository.save(product);
        if (becameLowStock) {
            productEventPublisher.publish(StockLowEvent.from(savedProduct));
        }
        return ProductResult.from(savedProduct);
    }

    @Transactional
    public void delete(UUID companyId, UUID productId) {
        productRepository.delete(findProduct(companyId, productId));
    }

    private Product findProduct(UUID companyId, UUID productId) {
        return productRepository.findByIdAndCompanyId(productId, companyId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
    }

    private void requireCompany(UUID companyId) {
        if (companyRepository.findById(companyId).isEmpty()) {
            throw new NotFoundException("Company not found: " + companyId);
        }
    }
}

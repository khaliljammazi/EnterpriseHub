package com.enterprisehub.backend.product.presentation;

import com.enterprisehub.backend.product.application.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies/{companyId}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@PathVariable UUID companyId, Authentication authentication,
                                  @Valid @RequestBody CreateProductRequest request) {
        return ProductResponse.from(productService.create(companyId, authentication.getName(), request.toCommand()));
    }

    @GetMapping
    public List<ProductResponse> findAll(@PathVariable UUID companyId) {
        return productService.findAll(companyId).stream().map(ProductResponse::from).toList();
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable UUID companyId, @PathVariable UUID productId,
                                  @Valid @RequestBody UpdateProductRequest request) {
        return ProductResponse.from(productService.update(companyId, productId, request.toCommand()));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID companyId, @PathVariable UUID productId) {
        productService.delete(companyId, productId);
    }
}

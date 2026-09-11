package com.ecommerce.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.ecommerce.order.dto.ProductInfo;
import com.ecommerce.order.exception.ProductNotFoundInCatalogException;

@Component
public class CatalogServiceClient {

    private final RestClient restClient;

    public CatalogServiceClient(@Value("${catalog-service.url}") String catalogServiceUrl) {
        this.restClient = RestClient.builder()
            .baseUrl(catalogServiceUrl)
            .build();
    }

    public ProductInfo getProduct(Long productId) {
        try {
            return restClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(ProductInfo.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundInCatalogException(productId);
        }
    }
}
package com.ecommerce.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.catalog.dto.ProductRequest;
import com.ecommerce.catalog.dto.ProductResponse;
import com.ecommerce.catalog.exception.ProductNotFoundException;
import com.ecommerce.catalog.model.Product;
import com.ecommerce.catalog.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_deberiaGuardarYDevolverElProductoCreado() {
        
        ProductRequest request = new ProductRequest();
        request.setName("Teclado mecánico");
        request.setDescription("Teclado mecánico RGB");
        request.setPrice(new BigDecimal("89.99"));
        request.setStock(10);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Teclado mecánico");
        savedProduct.setDescription("Teclado mecánico RGB");
        savedProduct.setPrice(new BigDecimal("89.99"));
        savedProduct.setStock(10);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Teclado mecánico");
        assertThat(response.getPrice()).isEqualByComparingTo(new BigDecimal("89.99"));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_deberiaLanzarExcepcion_cuandoElProductoNoExiste() {

        Long idInexistente = 999L;
        when(productRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(idInexistente));

        verify(productRepository, times(1)).findById(idInexistente);
    }
}
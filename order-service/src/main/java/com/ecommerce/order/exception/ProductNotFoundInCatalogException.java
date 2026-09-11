package com.ecommerce.order.exception;

public class ProductNotFoundInCatalogException extends RuntimeException {
    public ProductNotFoundInCatalogException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }
}

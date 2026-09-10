package com.ecommerce.catalog.dto;

import jakarta.validation.constraints.*;

public class ProductDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;
    
    @Positive(message = "El precio debe ser positivo")
    private Double price;
    
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

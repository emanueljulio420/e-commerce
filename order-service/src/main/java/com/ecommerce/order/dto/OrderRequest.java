package com.ecommerce.order.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class OrderRequest {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<OrderItemRequest> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
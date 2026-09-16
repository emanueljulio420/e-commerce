package com.ecommerce.order.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class OrderRequest {

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<OrderItemRequest> items;

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
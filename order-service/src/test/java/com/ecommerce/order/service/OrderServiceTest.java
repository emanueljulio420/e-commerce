package com.ecommerce.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.order.client.CatalogServiceClient;
import com.ecommerce.order.dto.*;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.exception.ProductNotFoundInCatalogException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogServiceClient catalogServiceClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_deberiaCalcularTotalYGuardarElPedido() {
        // Arrange
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setUserId(10L);
        request.setItems(List.of(itemRequest));

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("Mouse inalámbrico");
        productInfo.setPrice(new BigDecimal("45.50"));

        when(catalogServiceClient.getProduct(1L)).thenReturn(productInfo);

        // Simulamos que el repository, al guardar, devuelve el mismo Order que recibió,
        // pero ya con id asignado (como haría la base de datos real)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setId(100L);
            return orderToSave;
        });

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("91.00")); // 45.50 * 2
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getProductName()).isEqualTo("Mouse inalámbrico");

        verify(catalogServiceClient, times(1)).getProduct(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_deberiaLanzarExcepcion_cuandoElProductoNoExisteEnCatalogo() {
        // Arrange
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(999L);
        itemRequest.setQuantity(1);

        OrderRequest request = new OrderRequest();
        request.setUserId(10L);
        request.setItems(List.of(itemRequest));

        when(catalogServiceClient.getProduct(999L))
                .thenThrow(new ProductNotFoundInCatalogException(999L));

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(
                ProductNotFoundInCatalogException.class,
                () -> orderService.createOrder(request));

        // Confirmamos que, como el producto no existía, NUNCA se llegó a intentar
        // guardar el pedido
        verify(orderRepository, never()).save(any(Order.class));
    }
}
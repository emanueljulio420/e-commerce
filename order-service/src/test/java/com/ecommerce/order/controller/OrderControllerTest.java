package com.ecommerce.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.order.client.CatalogServiceClient;
import com.ecommerce.order.dto.ProductInfo;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CatalogServiceClient catalogServiceClient;

    @Test
    void createOrder_deberiaDevolver201_conElProductoObtenidoDelCatalogo() throws Exception {
        // Arrange: le decimos al bean mockeado qué responder cuando lo llamen
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("Mouse inalámbrico");
        productInfo.setPrice(new BigDecimal("45.50"));

        when(catalogServiceClient.getProduct(1L)).thenReturn(productInfo);

        String jsonValido = """
            {
                "userId": 10,
                "items": [
                    { "productId": 1, "quantity": 2 }
                ]
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", is(10)))
            .andExpect(jsonPath("$.totalAmount", is(91.00)))
            .andExpect(jsonPath("$.items[0].productName", is("Mouse inalámbrico")));
    }
}
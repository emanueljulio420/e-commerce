package com.ecommerce.order.controller;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import com.ecommerce.order.client.CatalogServiceClient;
import com.ecommerce.order.dto.ProductInfo;

@SpringBootTest
class OrderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockitoBean
    private CatalogServiceClient catalogServiceClient;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void createOrder_deberiaDevolver201_conElProductoObtenidoDelCatalogo() throws Exception {
        // Arrange: le decimos al bean mockeado qué responder cuando lo llamen

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("Mouse inalámbrico");
        productInfo.setPrice(new BigDecimal("45.50"));

        when(catalogServiceClient.getProduct(1L)).thenReturn(productInfo);

        String jsonValido = """
                {
                    "items": [
                        { "productId": 1, "quantity": 2 }
                    ]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .with(authenticatedUser(10L, "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated());
    }

    private RequestPostProcessor authenticatedUser(Long userId, String role) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "testuser", null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        auth.setDetails(userId);
        return SecurityMockMvcRequestPostProcessors.authentication(auth);
    }
}

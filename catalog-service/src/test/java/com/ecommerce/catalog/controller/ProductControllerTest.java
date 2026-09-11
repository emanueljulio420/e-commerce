package com.ecommerce.catalog.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct_deberiaDevolver400_cuandoNombreYPrecioFaltan() throws Exception {
        String jsonInvalido = """
                {
                    "name": "",
                    "description": "Producto de prueba",
                    "price": null,
                    "stock": 5
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("El nombre no puede estar vacío")))
                .andExpect(jsonPath("$.price", is("El precio es obligatorio")));
    }

    @Test
    void createProduct_deberiaDevolver201_cuandoElProductoEsValido() throws Exception {
        String jsonValido = """
                {
                    "name": "Mouse inalámbrico",
                    "description": "Mouse ergonómico inalámbrico",
                    "price": 45.50,
                    "stock": 20
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Mouse inalámbrico")))
                .andExpect(jsonPath("$.price", is(45.50)));
    }

    @Test
    void getProductById_deberiaDevolver404_cuandoElProductoNoExiste() throws Exception {
        mockMvc.perform(get("/api/products/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Producto no encontrado con ID: 99999")));
    }
}
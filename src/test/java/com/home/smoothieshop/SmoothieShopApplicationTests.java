package com.home.smoothieshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.dto.enums.ProductTypeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SmoothieShopApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenNonExistingProductId_whenGettingProductById_thenNotFoundStatusReturned() throws Exception {
        long nonExistingProductId = 48;

        mockMvc.perform(get("/api/v1/products/{id}", nonExistingProductId))
                .andExpect(status().isNotFound());
    }

    @Test
    void giveNewProduct_whenPostingNewProduct_thenStatusCreatedReturned() throws Exception {
        ProductDto productDto = createProductStub();

        mockMvc.perform(post("/api/v1/products")
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private long postNewProduct(ProductDto productDto) throws Exception {
        String createdProductIdResponse = mockMvc.perform(post("/api/v1/products")
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.parseLong(createdProductIdResponse);
    }

    private ProductDto createProductStub() {
        return new ProductDto(
                null,
                ProductTypeDto.SMOOTHIE,
                "Weekend fun",
                "Drink for those who are having fun time on the weekend",
                Collections.emptyList()
        );
    }

}

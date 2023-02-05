package com.home.smoothieshop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.smoothieshop.dto.NutritionalValueDto;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.dto.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
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
    void givenNewProduct_whenPostingNewProduct_thenStatusCreatedReturned() throws Exception {
        ProductDto productDto = createProductDtoStub(createNutritionalValuesDtoStub());

        mockMvc.perform(post("/api/v1/products")
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void givenNewProduct_whenPostingNewProductAndGettingSame_thenPostedDtoEqualsReturnedDto() throws Exception {
        ProductDto productDto = createProductDtoStub(createNutritionalValuesDtoStub());
        long productId = postNewProduct(productDto);

        String payload = mockMvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductDto returnedProduct = objectMapper.readValue(payload, ProductDto.class);

        assertThat(returnedProduct)
                .usingRecursiveComparison()
                .ignoringFields("id", "nutritionalValues.id")
                .isEqualTo(productDto);
    }

    @Test
    void given2newProducts_whenPostingThemAndGettingProductList_thenThose2CreateMustBeInTheList() throws Exception {
        ProductDto firstProductDto = createProductDtoStub(emptyList());
        ProductDto secondProductDto = createProductDtoStub(emptyList());
        long firstProductId = postNewProduct(firstProductDto);
        long secondProductId = postNewProduct(secondProductDto);
        var expectedProductList = List.of(
                copyWithId(firstProductId, firstProductDto),
                copyWithId(secondProductId, secondProductDto)
        );

        String payload = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDto> returnedProductList = objectMapper.readValue(payload, new TypeReference<List<ProductDto>>() {
        });

        assertThat(returnedProductList)
                .containsAll(expectedProductList);
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

    private ProductDto createProductDtoStub(List<NutritionalValueDto> nutritionalValues) {
        return new ProductDto(
                null,
                ProductTypeDto.SMOOTHIE,
                "Weekend fun",
                "Drink for those who are having fun time on the weekend",
                nutritionalValues
        );
    }

    private List<NutritionalValueDto> createNutritionalValuesDtoStub() {
        return List.of(
                createMacroNutritionalValueDtoStub(),
                createMicroNutritionalValueDtoStub()
        );
    }

    private NutritionalValueDto createMacroNutritionalValueDtoStub() {
        return new NutritionalValueDto(
                null,
                NutrientTypeDto.MACRO,
                MacroNutrientDto.FAT,
                null,
                NutrientUnitDto.G,
                5.4
        );
    }

    private NutritionalValueDto createMicroNutritionalValueDtoStub() {
        return new NutritionalValueDto(
                null,
                NutrientTypeDto.MICRO,
                null,
                MicroNutrientDto.VIT_A,
                NutrientUnitDto.MG,
                180.0
        );
    }

    private ProductDto copyWithId(long id, ProductDto productDto) {
        return new ProductDto(
                id,
                productDto.productType(),
                productDto.name(),
                productDto.basicDetails(),
                productDto.nutritionalValues()
        );
    }
}

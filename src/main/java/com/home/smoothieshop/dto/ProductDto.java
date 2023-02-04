package com.home.smoothieshop.dto;

import com.home.smoothieshop.dto.enums.ProductTypeDto;

import java.util.List;

public record ProductDto(long id,
                         ProductTypeDto productType,
                         String name,
                         String basicDetails,
                         List<NutritionalValueDto> nutritionalValues) {
}
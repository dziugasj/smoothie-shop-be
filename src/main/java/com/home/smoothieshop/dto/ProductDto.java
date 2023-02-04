package com.home.smoothieshop.dto;

import com.home.smoothieshop.dto.enums.ProductTypeDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record ProductDto(Long id,
                         @NotNull ProductTypeDto productType,
                         @NotNull String name,
                         @NotNull String basicDetails,
                         @NotNull List<NutritionalValueDto> nutritionalValues) {
}
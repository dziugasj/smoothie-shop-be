package com.home.smoothieshop.dto;

import com.home.smoothieshop.dto.enums.MacroNutrientDto;
import com.home.smoothieshop.dto.enums.MicroNutrientDto;
import com.home.smoothieshop.dto.enums.NutrientTypeDto;
import com.home.smoothieshop.dto.enums.NutrientUnitDto;

import javax.validation.constraints.NotNull;

public record NutritionalValueDto(Long id,
                                  @NotNull NutrientTypeDto nutrientType,
                                  MacroNutrientDto macroNutrient,
                                  MicroNutrientDto microNutrient,
                                  @NotNull NutrientUnitDto nutrientUnit,
                                  @NotNull Double nutrientValue
) {
}
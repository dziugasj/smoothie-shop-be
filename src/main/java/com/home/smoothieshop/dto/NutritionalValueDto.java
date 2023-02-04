package com.home.smoothieshop.dto;

import com.home.smoothieshop.dto.enums.MacroNutrientDto;
import com.home.smoothieshop.dto.enums.MicroNutrientDto;
import com.home.smoothieshop.dto.enums.NutrientTypeDto;
import com.home.smoothieshop.dto.enums.NutrientUnitDto;

public record NutritionalValueDto(Long id,
                                  NutrientTypeDto nutrientType,
                                  MacroNutrientDto macroNutrient,
                                  MicroNutrientDto microNutrient,
                                  NutrientUnitDto nutrientUnit,
                                  double nutrientValue
) {
}
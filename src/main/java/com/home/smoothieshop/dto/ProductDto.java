package com.home.smoothieshop.dto;

import java.util.List;

public record ProductDto(long id, String name, List<NutritionalValueDto> details) {
}

package com.home.smoothieshop.service;

import com.home.smoothieshop.dto.NutritionalValueDto;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.dto.enums.*;
import com.home.smoothieshop.exceptions.NotFoundException;
import com.home.smoothieshop.model.NutritionalValue;
import com.home.smoothieshop.model.Product;
import com.home.smoothieshop.model.ProductRepository;
import com.home.smoothieshop.model.enums.MacroNutrient;
import com.home.smoothieshop.model.enums.MicroNutrient;
import com.home.smoothieshop.model.enums.NutrientType;
import com.home.smoothieshop.model.enums.NutrientUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional(readOnly = true)
    public ProductDto getProduct(long id) {
        return productRepository.findById(id)
                .map(this::toProductDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductDto)
                .toList();
    }

    @Transactional
    public void updateProductNutritionalValues(long productId, List<NutritionalValueDto> valuesDto) {
        Product product = findProduct(productId);
        var dtoGroupedById = groupByNutritionalValueDtoById(valuesDto);
        removeNutritionalValues(product, dtoGroupedById);
        updateNutritionalValues(product, dtoGroupedById);
        addNutritionalValues(product, valuesDto);

        productRepository.save(product);
    }

    private Product findProduct(long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(NotFoundException::new);
    }

    private Map<Long, List<NutritionalValueDto>> groupByNutritionalValueDtoById(List<NutritionalValueDto> valuesDto) {
        return valuesDto
                .stream()
                .collect(groupingBy(NutritionalValueDto::id));
    }

    private void updateNutritionalValues(Product product, Map<Long, List<NutritionalValueDto>> dtoGroupedById) {
        product.getNutritionalValues()
                .stream()
                .filter(value -> dtoGroupedById.containsKey(value.getId()))
                .forEach(value -> updateNutritionalValue(value, dtoGroupedById.get(value.getId()).get(0)));
    }

    private void removeNutritionalValues(Product product, Map<Long, List<NutritionalValueDto>> dtoGroupedById) {
        product.getNutritionalValues()
                .stream()
                .filter(value -> !dtoGroupedById.containsKey(value.getId()))
                .forEach(product::removeNutritionalValue);
    }

    private void addNutritionalValues(Product product, List<NutritionalValueDto> valuesDto) {
        valuesDto
                .stream()
                .filter(dto -> isNull(dto.id()))
                .forEach(dto -> product.addNutritionalValue(toNutritionalValueEntity(dto)));
    }

    private void updateNutritionalValue(NutritionalValue oldValue, NutritionalValueDto dto) {
        NutritionalValue newValue = toNutritionalValueEntity(dto);

        oldValue.setNutrientType(newValue.getNutrientType());
        oldValue.setMacroNutrient(newValue.getMacroNutrient());
        oldValue.setMicroNutrient(newValue.getMicroNutrient());
        oldValue.setNutrientUnit(newValue.getNutrientUnit());
        oldValue.setNutrientValue(newValue.getNutrientValue());
    }

    private NutritionalValue toNutritionalValueEntity(NutritionalValueDto dto) {
        NutritionalValue value = new NutritionalValue();
        value.setNutrientType(NutrientType.valueOf(dto.nutrientType().name()));
        value.setMacroNutrient(MacroNutrient.valueOf(dto.macroNutrient().name()));
        value.setMicroNutrient(MicroNutrient.valueOf(dto.microNutrient().name()));
        value.setNutrientUnit(NutrientUnit.valueOf(dto.nutrientUnit().name()));
        value.setNutrientValue(dto.nutrientValue());

        return value;
    }

    private ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                ProductTypeDto.valueOf(product.getProductType().name()),
                product.getName(),
                product.getBasicDetails(),
                toNutritionalValuesDto(product.getNutritionalValues())
        );
    }

    private List<NutritionalValueDto> toNutritionalValuesDto(List<NutritionalValue> values) {
        return values
                .stream()
                .map(this::toNutritionalValueDto)
                .toList();
    }

    private NutritionalValueDto toNutritionalValueDto(NutritionalValue value) {
        return new NutritionalValueDto(
                value.getId(),
                NutrientTypeDto.valueOf(value.getNutrientType().name()),
                MacroNutrientDto.valueOf(value.getMacroNutrient().name()),
                MicroNutrientDto.valueOf(value.getMicroNutrient().name()),
                NutrientUnitDto.valueOf(value.getNutrientUnit().name()),
                value.getNutrientValue()
        );
    }
}
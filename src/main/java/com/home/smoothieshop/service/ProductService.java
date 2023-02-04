package com.home.smoothieshop.service;

import com.home.smoothieshop.dto.NutritionalValueDto;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.dto.enums.*;
import com.home.smoothieshop.exceptions.NotFoundException;
import com.home.smoothieshop.model.NutritionalValue;
import com.home.smoothieshop.model.Product;
import com.home.smoothieshop.model.ProductRepository;
import com.home.smoothieshop.model.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

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
    public long createProduct(ProductDto productPostDto) {
        Product product = toProductEntity(productPostDto);
        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    @Transactional
    public void updateProduct(long productId, ProductDto productDto) {
        Product product = findProduct(productId);
        mapBaseProductFields(product, productDto);
        mapNutritionalValues(product, productDto.nutritionalValues());

        productRepository.save(product);
    }

    @Transactional
    public void updateProductNutritionalValues(long productId, List<NutritionalValueDto> valuesDto) {
        Product product = findProduct(productId);
        mapNutritionalValues(product, valuesDto);

        productRepository.save(product);
    }

    private Product findProduct(long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(NotFoundException::new);
    }

    private void mapNutritionalValues(Product product, List<NutritionalValueDto> valuesDto) {
        var dtoGroupedById = groupByNutritionalValueDtoById(valuesDto);
        removeNutritionalValues(product, dtoGroupedById);
        updateNutritionalValues(product, dtoGroupedById);
        addNutritionalValues(product, valuesDto);
    }

    private Map<Long, List<NutritionalValueDto>> groupByNutritionalValueDtoById(List<NutritionalValueDto> valuesDto) {
        return valuesDto
                .stream()
                .filter(dto -> Objects.nonNull(dto.id()))
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
                .removeIf(value -> !dtoGroupedById.containsKey(value.getId()));
    }

    private void addNutritionalValues(Product product, List<NutritionalValueDto> valuesDto) {
        valuesDto
                .stream()
                .filter(dto -> isNull(dto.id()))
                .forEach(dto -> addNutritionalValue(product, toNutritionalValueEntity(dto)));
    }

    private void addNutritionalValue(Product product, NutritionalValue nutritionalValue) {
        product.addNutritionalValue(nutritionalValue);
        logger.info("NutritionalValue added [productId={} nutritionalValue={}]", product.getId(), nutritionalValue);
    }

    private void updateNutritionalValue(NutritionalValue valueToUpdate, NutritionalValueDto dto) {
        NutritionalValue newValue = toNutritionalValueEntity(dto);

        // NutritionalValue .equals() method cannot be used here since it implements equality check by id only
        if (Objects.equals(valueToUpdate.getNutrientType(), newValue.getNutrientType()) &&
                Objects.equals(valueToUpdate.getMacroNutrient(), newValue.getMacroNutrient()) &&
                Objects.equals(valueToUpdate.getMicroNutrient(), newValue.getMicroNutrient()) &&
                Objects.equals(valueToUpdate.getNutrientUnit(), newValue.getNutrientUnit()) &&
                Objects.equals(valueToUpdate.getNutrientValue(), newValue.getNutrientValue())
        ) {
            // Value is unchanged
        } else {
            valueToUpdate.setNutrientType(newValue.getNutrientType());
            valueToUpdate.setMacroNutrient(newValue.getMacroNutrient());
            valueToUpdate.setMicroNutrient(newValue.getMicroNutrient());
            valueToUpdate.setNutrientUnit(newValue.getNutrientUnit());
            valueToUpdate.setNutrientValue(newValue.getNutrientValue());

            logger.info("NutritionalValue updated [productId={} nutritionalValue={}]", valueToUpdate.getProduct().getId(), valueToUpdate);
        }
    }

    private NutritionalValue toNutritionalValueEntity(NutritionalValueDto dto) {
        NutritionalValue value = new NutritionalValue();
        value.setNutrientType(NutrientType.valueOf(dto.nutrientType().name()));
        value.setMacroNutrient(toMacroNutrient(dto.macroNutrient()));
        value.setMicroNutrient(toMicroNutrient(dto.microNutrient()));
        value.setNutrientUnit(NutrientUnit.valueOf(dto.nutrientUnit().name()));
        value.setNutrientValue(dto.nutrientValue());

        return value;
    }

    private MacroNutrient toMacroNutrient(MacroNutrientDto dto) {
        return isNull(dto) ? null : MacroNutrient.valueOf(dto.name());
    }

    private MicroNutrient toMicroNutrient(MicroNutrientDto dto) {
        return isNull(dto) ? null : MicroNutrient.valueOf(dto.name());
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
                toMacroNutrientDto(value.getMacroNutrient()),
                toMicroNutrientDto(value.getMicroNutrient()),
                NutrientUnitDto.valueOf(value.getNutrientUnit().name()),
                value.getNutrientValue()
        );
    }

    private MacroNutrientDto toMacroNutrientDto(MacroNutrient value) {
        return isNull(value) ? null : MacroNutrientDto.valueOf(value.name());
    }

    private MicroNutrientDto toMicroNutrientDto(MicroNutrient value) {
        return isNull(value) ? null : MicroNutrientDto.valueOf(value.name());
    }

    private Product toProductEntity(ProductDto productDto) {
        Product product = new Product();
        mapBaseProductFields(product, productDto);
        productDto.nutritionalValues()
                .forEach(dtoNutritionalValue -> product.addNutritionalValue(toNutritionalValueEntity(dtoNutritionalValue)));

        return product;
    }

    private void mapBaseProductFields(Product product, ProductDto productDto) {
        product.setProductType(ProductType.valueOf(productDto.productType().name()));
        product.setName(productDto.name());
        product.setBasicDetails(productDto.basicDetails());
    }
}
package com.home.smoothieshop.service;

import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.exceptions.NotFoundException;
import com.home.smoothieshop.model.Product;
import com.home.smoothieshop.model.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto getProduct(long id) {
        return productRepository.findById(id)
                .map(this::toProductDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductDto)
                .toList();
    }

    private ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(), product.getName() , Collections.emptyList());
    }
}

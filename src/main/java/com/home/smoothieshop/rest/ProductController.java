package com.home.smoothieshop.rest;

import com.home.smoothieshop.dto.NutritionalValueDto;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ProductDto getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/products")
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @PostMapping("/products")
    public ResponseEntity<Long> createProduct(@Valid @RequestBody ProductDto productDto) {
        long productId = productService.createProduct(productDto);

        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public void updateProduct(@PathVariable long id, @Valid @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
    }

    @PutMapping("/products/{id}/details")
    public void updateProductDetails(@PathVariable long id, @Valid @RequestBody List<NutritionalValueDto> nutritionalValueDto) {
        productService.updateProductNutritionalValues(id, nutritionalValueDto);
    }
}

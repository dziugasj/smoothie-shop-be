package com.home.smoothieshop.rest;

import com.home.smoothieshop.dto.ProductDetailsDto;
import com.home.smoothieshop.dto.ProductDto;
import com.home.smoothieshop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/products/{id}/details")
    public void updateProductDetails(@PathVariable long id, @RequestBody ProductDetailsDto productDetailsDto) {




    }

}

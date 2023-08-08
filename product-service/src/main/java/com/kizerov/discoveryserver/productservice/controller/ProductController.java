package com.kizerov.discoveryserver.productservice.controller;

import com.kizerov.discoveryserver.productservice.service.ProductService;
import com.kizerov.discoveryserver.productservice.dto.ProductRequest;
import com.kizerov.discoveryserver.productservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {

        productService.createProduct(productRequest);
    }
}

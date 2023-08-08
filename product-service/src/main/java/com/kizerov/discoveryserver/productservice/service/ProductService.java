package com.kizerov.discoveryserver.productservice.service;

import com.kizerov.discoveryserver.productservice.repository.ProductRepository;
import com.kizerov.discoveryserver.productservice.dto.ProductRequest;
import com.kizerov.discoveryserver.productservice.dto.ProductResponse;
import com.kizerov.discoveryserver.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product with id: {} and name {} was save",product.getId(),product.getName());
    }

    public List<ProductResponse> getAllProducts() {

        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(this::productToProductResponse)
                .toList();
    }

    private ProductResponse productToProductResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

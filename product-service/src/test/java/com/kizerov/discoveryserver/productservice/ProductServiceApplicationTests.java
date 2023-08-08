package com.kizerov.discoveryserver.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kizerov.discoveryserver.productservice.dto.ProductRequest;
import com.kizerov.discoveryserver.productservice.model.Product;
import com.kizerov.discoveryserver.productservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void loadData() {

        productRepository.save(new Product("1234","iphone","iphone 12",BigDecimal.valueOf(1000)));
    }

    @AfterEach
    void clearData() {

        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        
        ProductRequest productRequest = getProductRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(2,productRepository.findAll().size());
        Assertions.assertEquals(productRequest.getDescription(),productRepository.findProductByDescription(productRequest.getDescription()).getDescription());
    }

    @Test
    void shouldGetAllProducts() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(1,productRepository.findAll().size());
        Assertions.assertTrue(productRepository.findById("1234").isPresent());
    }

    private ProductRequest getProductRequest() {

        return ProductRequest.builder()
                .name("iphone")
                .description("iphone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

}
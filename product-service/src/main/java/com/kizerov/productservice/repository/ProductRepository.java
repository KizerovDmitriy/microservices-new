package com.kizerov.productservice.repository;

import com.kizerov.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {

    Product findProductByDescription(String description);
}

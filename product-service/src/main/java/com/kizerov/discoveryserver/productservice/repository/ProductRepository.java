package com.kizerov.discoveryserver.productservice.repository;

import com.kizerov.discoveryserver.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {

    Product findProductByDescription(String description);
}

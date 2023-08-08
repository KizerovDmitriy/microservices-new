package com.kizerov.discoveryserver.orderservice.repository;

import com.kizerov.discoveryserver.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

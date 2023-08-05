package com.kizerov.orderservice.service;

import com.kizerov.orderservice.dto.OrderLineItemsDTO;
import com.kizerov.orderservice.dto.OrderRequest;
import com.kizerov.orderservice.model.Order;
import com.kizerov.orderservice.model.OrderLineItems;
import com.kizerov.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> list = orderRequest.getOrderLineItemsList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(list);

        orderRepository.save(order);

        log.info("Order {} was save",order.getId());
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {

        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());

        return orderLineItems;
    }
}

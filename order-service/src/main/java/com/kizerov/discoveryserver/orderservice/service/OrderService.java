package com.kizerov.discoveryserver.orderservice.service;

import com.kizerov.discoveryserver.orderservice.repository.OrderRepository;
import com.kizerov.discoveryserver.orderservice.dto.InventoryResponse;
import com.kizerov.discoveryserver.orderservice.dto.OrderLineItemsDTO;
import com.kizerov.discoveryserver.orderservice.dto.OrderRequest;
import com.kizerov.discoveryserver.orderservice.model.Order;
import com.kizerov.discoveryserver.orderservice.model.OrderLineItems;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> list = orderRequest.getOrderLineItemsList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(list);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //Call inventory service

        InventoryResponse [] inventoryResponsesArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray != null ? inventoryResponsesArray : new InventoryResponse[0])
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {

            orderRepository.save(order);

        } else {

            throw new IllegalArgumentException("Out of stock");
        }

        log.info("Order {} was save", order.getId());
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {

        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());

        return orderLineItems;
    }
}

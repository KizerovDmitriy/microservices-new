package com.kizerov.discoveryserver.orderservice.controller;

import com.kizerov.discoveryserver.orderservice.service.OrderService;
import com.kizerov.discoveryserver.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {

        orderService.placeOrder(orderRequest);

        return "Order place successfully";
    }
}

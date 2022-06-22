package com.github.caiquetgr.orders.entrypoint.rest;

import com.github.caiquetgr.orders.entrypoint.rest.mapper.OrderMapper;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import com.github.caiquetgr.orders.entrypoint.rest.response.OrderResponse;
import com.github.caiquetgr.orders.usecase.CreateOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@RestController
class OrderRestControllerV1 implements OrderControllerV1 {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return Optional.ofNullable(orderRequest)
                .map(orderMapper::orderRequestToDomain)
                .map(createOrderUseCase::create)
                .map(orderMapper::domainToOrderResponse)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .orElse(ResponseEntity.notFound().build());
    }

}

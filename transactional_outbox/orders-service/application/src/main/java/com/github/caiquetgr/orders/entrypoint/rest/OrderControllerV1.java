package com.github.caiquetgr.orders.entrypoint.rest;

import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import com.github.caiquetgr.orders.entrypoint.rest.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface OrderControllerV1 {

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(final OrderRequest orderRequest);

}

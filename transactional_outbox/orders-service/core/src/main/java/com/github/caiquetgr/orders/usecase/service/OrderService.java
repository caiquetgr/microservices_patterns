package com.github.caiquetgr.orders.usecase.service;

import com.github.caiquetgr.orders.dataprovider.OrderDataProvider;
import com.github.caiquetgr.orders.domain.Order;
import com.github.caiquetgr.orders.usecase.CreateOrderUseCase;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;
import javax.validation.ConstraintViolationException;

@Named
@RequiredArgsConstructor
class OrderService implements CreateOrderUseCase {

    private final OrderDataProvider orderDataProvider;

    @Override
    public Order create(final Order order) throws ConstraintViolationException {
        order.validateOrderToCreate();
        return orderDataProvider.create(order);
    }
}

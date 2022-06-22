package com.github.caiquetgr.orders.usecase;

import com.github.caiquetgr.orders.domain.Order;

import javax.validation.ConstraintViolationException;

public interface CreateOrderUseCase {

    Order create(final Order order) throws ConstraintViolationException;

}

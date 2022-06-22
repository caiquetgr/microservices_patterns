package com.github.caiquetgr.orders.asserts;

import com.github.caiquetgr.orders.domain.Order;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class OrderAssert extends AbstractAssert<OrderAssert, Order> {
    protected OrderAssert(final Order order) {
        super(order, OrderAssert.class);
    }

    public static OrderAssert assertThat(final Order order) {
        return new OrderAssert(order);
    }

    public OrderAssert hasId() {
        Assertions.assertThat(actual.getId()).isNotNull();
        return this;
    }

    public OrderAssert isEqualToOrderRequestIgnoringId(final OrderRequest orderRequest) {
        this.usingRecursiveComparison()
                .ignoringFields("id", "validator")
                .isEqualTo(orderRequest);
        return this;
    }

    public OrderAssert isEqualToOrderIgnoringId(final Order order) {
        this.usingRecursiveComparison()
                .ignoringFields("id", "validator")
                .isEqualTo(order);
        return this;
    }
}

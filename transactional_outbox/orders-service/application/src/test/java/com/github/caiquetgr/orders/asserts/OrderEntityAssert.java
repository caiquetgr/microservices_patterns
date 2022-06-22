package com.github.caiquetgr.orders.asserts;

import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.domain.Order;
import org.assertj.core.api.AbstractAssert;

public class OrderEntityAssert extends AbstractAssert<OrderEntityAssert, OrderEntity> {
    protected OrderEntityAssert(final OrderEntity orderEntity) {
        super(orderEntity, OrderEntityAssert.class);
    }

    public static OrderEntityAssert assertThat(final OrderEntity orderEntity) {
        return new OrderEntityAssert(orderEntity);
    }

    public OrderEntityAssert isEqualToOrderIgnoringId(final Order order) {
        this.usingRecursiveComparison()
                .ignoringFields("id", "validator")
                .isEqualTo(order);
        return this;
    }
}

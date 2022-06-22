package com.github.caiquetgr.orders.asserts;

import com.github.caiquetgr.orders.dataprovider.entity.OrderTransactionalOutboxEntity;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class OrderTransactionalOutboxEntityAssert
        extends AbstractAssert<OrderTransactionalOutboxEntityAssert, OrderTransactionalOutboxEntity> {
    protected OrderTransactionalOutboxEntityAssert(final OrderTransactionalOutboxEntity orderEntity) {
        super(orderEntity, OrderTransactionalOutboxEntityAssert.class);
    }

    public static OrderTransactionalOutboxEntityAssert assertThat(final OrderTransactionalOutboxEntity orderEntity) {
        return new OrderTransactionalOutboxEntityAssert(orderEntity);
    }

    public OrderTransactionalOutboxEntityAssert hasId() {
        Assertions.assertThat(actual.getId()).isNotNull();
        return this;
    }

    public OrderTransactionalOutboxEntityAssert hasOrderEqualTo(final String jsonOrderEntity) {
        Assertions.assertThat(actual.getOrder()).isEqualTo(jsonOrderEntity);
        return this;
    }

    public OrderTransactionalOutboxEntityAssert hasCreatedAt() {
        Assertions.assertThat(actual.getCreatedAt()).isNotNull();
        return this;
    }
}

package com.github.caiquetgr.orders.asserts;

import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class OrderEntityIntegrationTestAssert extends AbstractAssert<OrderEntityIntegrationTestAssert, OrderEntity> {
    public OrderEntityIntegrationTestAssert(final OrderEntity orderEntity) {
        super(orderEntity, OrderEntityIntegrationTestAssert.class);
    }

    public static OrderEntityIntegrationTestAssert assertThat(final OrderEntity orderEntity) {
        return new OrderEntityIntegrationTestAssert(orderEntity);
    }

    public OrderEntityIntegrationTestAssert isEqualToOrderRequest(final OrderRequest orderRequest) {
        this.usingRecursiveComparison()
                .ignoringFields("id", "installmentsValue", "totalValue")
                .isEqualTo(orderRequest);

        Assertions.assertThat(actual.getInstallmentsValue())
                .isEqualByComparingTo(orderRequest.getInstallmentsValue());
        Assertions.assertThat(actual.getTotalValue())
                .isEqualByComparingTo(orderRequest.getTotalValue());

        return this;
    }
}

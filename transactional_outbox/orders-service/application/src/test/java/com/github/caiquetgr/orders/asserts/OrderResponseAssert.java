package com.github.caiquetgr.orders.asserts;

import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import com.github.caiquetgr.orders.entrypoint.rest.response.OrderResponse;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

public class OrderResponseAssert extends AbstractAssert<OrderResponseAssert, OrderResponse> {
    protected OrderResponseAssert(final OrderResponse orderResponse) {
        super(orderResponse, OrderResponseAssert.class);
    }

    public static OrderResponseAssert assertThat(final OrderResponse orderResponse) {
        return new OrderResponseAssert(orderResponse);
    }

    public OrderResponseAssert hasId() {
        if (Objects.isNull(actual.getId())) {
            failWithMessage("Expected OrderResponse to have a id, but it was null");
        }
        return this;
    }

    public OrderResponseAssert isEqualToOrderRequestIgnoringId(final OrderRequest orderRequest) {
        this.usingRecursiveComparison()
                .ignoringFields("id", "validator")
                .isEqualTo(orderRequest);
        return this;
    }
}

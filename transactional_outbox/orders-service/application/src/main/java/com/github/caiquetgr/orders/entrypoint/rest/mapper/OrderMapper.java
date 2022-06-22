package com.github.caiquetgr.orders.entrypoint.rest.mapper;

import com.github.caiquetgr.orders.domain.Order;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import com.github.caiquetgr.orders.entrypoint.rest.response.OrderResponse;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order orderRequestToDomain(final OrderRequest orderRequest);

    OrderResponse domainToOrderResponse(final Order order);

}

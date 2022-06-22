package com.github.caiquetgr.orders.dataprovider.mapper;

import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.domain.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderEntityMapper {

    OrderEntity fromDomainToEntity(final Order order);

    Order fromEntityToDomain(final OrderEntity orderEntity);

}

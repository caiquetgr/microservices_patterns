package com.github.caiquetgr.orders.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.dataprovider.entity.OrderTransactionalOutboxEntity;
import com.github.caiquetgr.orders.dataprovider.mapper.OrderEntityMapper;
import com.github.caiquetgr.orders.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class OrderRepository implements OrderDataProvider {

    private final OrderDataJpaRepository orderDataJpaRepository;
    private final OrderTransactionalOutboxRepository orderTransactionalOutboxRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Order create(final Order order) {
        final var optionalOrder = Optional.ofNullable(order);

        if (optionalOrder.isEmpty()) {
            throw new InvalidParameterException("Order must not be null");
        }

        return optionalOrder
                .map(orderEntityMapper::fromDomainToEntity)
                .map(this::saveOrderAndOrderOutbox)
                .map(orderEntityMapper::fromEntityToDomain)
                .orElse(null);
    }

    private OrderEntity saveOrderAndOrderOutbox(final OrderEntity orderEntity) {
        orderTransactionalOutboxRepository.save(buildOrderTransactionalOutbox(orderDataJpaRepository.save(orderEntity)));
        return orderEntity;
    }

    @SneakyThrows
    private OrderTransactionalOutboxEntity buildOrderTransactionalOutbox(final OrderEntity orderEntity) {
        return OrderTransactionalOutboxEntity.builder()
                .order(objectMapper.writeValueAsString(orderEntity))
                .build();
    }
}

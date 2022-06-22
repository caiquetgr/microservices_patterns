package com.github.caiquetgr.orders.usecase.service;

import com.github.caiquetgr.orders.dataprovider.OrderDataProvider;
import com.github.caiquetgr.orders.domain.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDataProvider orderDataProvider;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderWhenOrderIsValid() {
        final Order orderToCreate = spy(validOrderToCreate());
        final Order orderCreated = validOrderCreated();

        doReturn(orderCreated)
                .when(orderDataProvider).create(orderToCreate);

        final Order orderReturned = orderService.create(orderToCreate);

        assertThat(orderReturned).isEqualTo(orderCreated);

        verify(orderToCreate).validateOrderToCreate();
        verify(orderDataProvider).create(orderToCreate);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsInvalid() {
        final Order order = spy(invalidOrderToCreate());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(ConstraintViolationException.class);

        verify(order).validateOrderToCreate();
        verify(orderDataProvider, never()).create(order);
    }

    private static Order validOrderToCreate() {
        return Order.builder()
                .item("TV")
                .totalValue(new BigDecimal("1000.99"))
                .installmentsValue(new BigDecimal("83.41583"))
                .installmentsQuantity(Integer.valueOf(12))
                .build();
    }

    private static Order validOrderCreated() {
        return validOrderToCreate()
                .toBuilder()
                .id(1L)
                .build();
    }

    private static Order invalidOrderToCreate() {
        return new Order();
    }
}

package com.github.caiquetgr.orders.dataprovider;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.dataprovider.entity.OrderTransactionalOutboxEntity;
import com.github.caiquetgr.orders.dataprovider.mapper.OrderEntityMapperImpl;
import com.github.caiquetgr.orders.domain.Order;
import com.github.caiquetgr.orders.fixture.OrderTemplateLoader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.InvalidParameterException;

import static com.github.caiquetgr.orders.asserts.OrderAssert.assertThat;
import static com.github.caiquetgr.orders.asserts.OrderEntityAssert.assertThat;
import static com.github.caiquetgr.orders.asserts.OrderTransactionalOutboxEntityAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({OrderRepository.class, OrderEntityMapperImpl.class, ObjectMapper.class})
@RequiredArgsConstructor
@DataJpaTest
class OrderRepositoryTest {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    @SpyBean
    private final OrderDataJpaRepository orderDataJpaRepository;
    @SpyBean
    private final OrderTransactionalOutboxRepository orderTransactionalOutboxRepository;

    @Captor
    private ArgumentCaptor<OrderEntity> orderEntityCaptor;

    @Captor
    private ArgumentCaptor<OrderTransactionalOutboxEntity> orderOutboxCaptor;


    @BeforeAll
    static void beforeAll() {
        FixtureFactoryLoader.loadTemplates("com.github.caiquetgr.orders.fixture");
    }

    @SneakyThrows
    @Test
    void shouldSaveOrderSuccessfully() {

        final Order order = Fixture.from(Order.class).gimme(OrderTemplateLoader.VALID_BEFORE_CREATE);

        final Order savedOrder = orderRepository.create(order);

        assertThat(savedOrder)
                .isNotNull()
                .hasId()
                .isEqualToOrderIgnoringId(order);

        verify(orderDataJpaRepository).save(orderEntityCaptor.capture());
        verify(orderTransactionalOutboxRepository).save(orderOutboxCaptor.capture());

        final OrderEntity orderEntity = orderEntityCaptor.getValue();

        assertThat(orderEntity)
                .isNotNull()
                .isEqualToOrderIgnoringId(order);
        assertThat(orderOutboxCaptor.getValue())
                .hasId()
                .hasCreatedAt()
                .hasOrderEqualTo(objectMapper.writeValueAsString(orderEntity));
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        assertThatThrownBy(() -> orderRepository.create(null))
                .isInstanceOf(InvalidParameterException.class);
    }

}

package com.github.caiquetgr.orders.domain;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class OrderTest {

    @SneakyThrows
    @DisplayName("Should create order and validate to create successfully")
    @Test
    void shouldCreateOrderSuccessfully() {
        final String item = "TV";
        final Integer installments = Integer.valueOf(12);
        final BigDecimal installmentValue = new BigDecimal("83.41583");
        final BigDecimal totalValue = new BigDecimal("1000.99");

        final Order validOrder = createOrder(item, installments, installmentValue, totalValue);

        assertThat(validOrder)
                .isNotNull()
                .extracting(Order::getItem, Order::getTotalValue, Order::getInstallmentsValue, Order::getInstallmentsQuantity)
                .contains(item, installmentValue, totalValue, installments);

        assertThatCode(() -> validOrder.validateOrderToCreate())
                .doesNotThrowAnyException();
    }

    @MethodSource("shouldNotCreateOrderSuccessfullyWhenInvalidParameters")
    @ParameterizedTest(name = "Should not create order successfully {0}")
    void shouldNotCreateOrderSuccessfullyWhenInvalid(final String testCase, final Order order) {
        assertThatThrownBy(() -> order.validateOrderToCreate())
                .isInstanceOf(ConstraintViolationException.class);
    }

    private static Stream<Arguments> shouldNotCreateOrderSuccessfullyWhenInvalidParameters() {
        return Stream.of(
                Arguments.of("with empty item",
                        createOrder("", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without item",
                        createOrder(null, Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with zero installments",
                        createOrder("TV", Integer.valueOf(0), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with negative installments",
                        createOrder("TV", Integer.valueOf(-1), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without installments",
                        createOrder("TV", null, new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with zero installmentValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("0"), new BigDecimal("1000.99"))),
                Arguments.of("with negative installmentValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("-83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without installmentValue",
                        createOrder("TV", Integer.valueOf(12), null, new BigDecimal("1000.99"))),
                Arguments.of("without totalValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("0"))),
                Arguments.of("without totalValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("-1000.99"))),
                Arguments.of("without totalValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("83.41583"), null)),
                Arguments.of("with incorrect installmentsValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("87.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with incorrect totalValue",
                        createOrder("TV", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("1001.00")))
        );
    }

    private static Order createOrder(final String item, final Integer installments, final BigDecimal installmentValue,
                                     final BigDecimal totalValue) {
        return Order.builder()
                .item(item)
                .totalValue(totalValue)
                .installmentsValue(installmentValue)
                .installmentsQuantity(installments)
                .build();
    }

}

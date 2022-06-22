package com.github.caiquetgr.orders.entrypoint.rest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.github.caiquetgr.orders.domain.Order;
import com.github.caiquetgr.orders.entrypoint.rest.mapper.OrderMapper;
import com.github.caiquetgr.orders.entrypoint.rest.mapper.OrderMapperImpl;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import com.github.caiquetgr.orders.entrypoint.rest.response.OrderResponse;
import com.github.caiquetgr.orders.fixture.OrderRequestIntegrationTestTemplateLoader;
import com.github.caiquetgr.orders.fixture.OrderRequestTemplateLoader;
import com.github.caiquetgr.orders.fixture.OrderTemplateLoader;
import com.github.caiquetgr.orders.usecase.CreateOrderUseCase;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.github.caiquetgr.orders.asserts.OrderAssert.assertThat;
import static com.github.caiquetgr.orders.asserts.OrderResponseAssert.assertThat;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class OrderRestControllerV1Test {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Spy
    private OrderMapper orderMapper = new OrderMapperImpl();

    @InjectMocks
    private OrderRestControllerV1 orderRestControllerV1;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("com.github.caiquetgr.orders.fixture");
    }

    @BeforeEach
    void beforeEach() {
        RestAssuredMockMvc.standaloneSetup(orderRestControllerV1);
    }

    @SneakyThrows
    @DisplayName("Should return status 201 when order is valid")
    @Test
    void shouldReturnStatus201WhenOrderIsValid() {
        final OrderRequest orderRequest = Fixture.from(OrderRequest.class).gimme(OrderRequestTemplateLoader.VALID);
        final Order order = Fixture.from(Order.class).gimme(OrderTemplateLoader.VALID_AFTER_CREATE);

        doReturn(order).when(createOrderUseCase).create(any(Order.class));

        final OrderResponse orderResponse = given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/v1/orders")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderResponse.class);

        assertThat(orderResponse)
                .isNotNull()
                .hasId()
                .isEqualToOrderRequestIgnoringId(orderRequest);

        final var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(createOrderUseCase).create(orderCaptor.capture());

        assertThat(orderCaptor.getValue())
                .isNotNull()
                .isEqualToOrderRequestIgnoringId(orderRequest);
    }

    @DisplayName("Should return status 400 when order is not sent")
    @Test
    void shouldReturnStatus400WhenOrderIsNotSent() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/v1/orders")
                .then()
                .statusCode(400);

        verify(createOrderUseCase, never()).create(any(Order.class));
    }

    @MethodSource("shouldReturnStatus400WhenOrderHasInvalidFieldsParameters")
    @DisplayName("Should return status 400 when order has invalid fields")
    @ParameterizedTest(name = "Should return status 400 when order has {0}")
    void shouldReturnStatus400WhenOrderHasInvalidFields(final String testCaseName, final OrderRequest orderRequest) {
        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/v1/orders")
                .then()
                .statusCode(400);

        verify(createOrderUseCase, never()).create(any(Order.class));
    }

    private static Stream<Arguments> shouldReturnStatus400WhenOrderHasInvalidFieldsParameters() {
        return Stream.of(
                Arguments.of("with empty item",
                        createOrderRequest("", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without item",
                        createOrderRequest(null, Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with zero installments",
                        createOrderRequest("TV", Integer.valueOf(0), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with negative installments",
                        createOrderRequest("TV", Integer.valueOf(-1), new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without installments",
                        createOrderRequest("TV", null, new BigDecimal("83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("with zero installmentValue",
                        createOrderRequest("TV", Integer.valueOf(12), new BigDecimal("0"), new BigDecimal("1000.99"))),
                Arguments.of("with negative installmentValue",
                        createOrderRequest("TV", Integer.valueOf(12), new BigDecimal("-83.41583"), new BigDecimal("1000.99"))),
                Arguments.of("without installmentValue",
                        createOrderRequest("TV", Integer.valueOf(12), null, new BigDecimal("1000.99"))),
                Arguments.of("without totalValue",
                        createOrderRequest("TV", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("0"))),
                Arguments.of("without totalValue",
                        createOrderRequest("TV", Integer.valueOf(12), new BigDecimal("83.41583"), new BigDecimal("-1000.99"))),
                Arguments.of("without totalValue",
                        createOrderRequest("TV", Integer.valueOf(12), new BigDecimal("83.41583"), null))
        );
    }

    private static OrderRequest createOrderRequest(final String item,
                                                   final Integer installments,
                                                   final BigDecimal installmentValue,
                                                   final BigDecimal totalValue) {
        return new OrderRequest(item, totalValue, installmentValue, installments);
    }

}

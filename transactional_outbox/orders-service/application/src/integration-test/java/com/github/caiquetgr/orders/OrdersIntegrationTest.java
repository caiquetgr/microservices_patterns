package com.github.caiquetgr.orders;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.caiquetgr.orders.asserts.OrderEntityIntegrationTestAssert;
import com.github.caiquetgr.orders.dataprovider.OrderDataJpaRepository;
import com.github.caiquetgr.orders.dataprovider.OrderTransactionalOutboxRepository;
import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import com.github.caiquetgr.orders.dataprovider.entity.OrderTransactionalOutboxEntity;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.caiquetgr.orders.fixture.OrderRequestIntegrationTestTemplateLoader.VALID_IT;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration-test")
public class OrdersIntegrationTest extends AbstractIntegrationTest {

    private final OrderDataJpaRepository orderDataJpaRepository;
    private final OrderTransactionalOutboxRepository orderTransactionalOutboxRepository;

    public OrdersIntegrationTest(
            final OrderDataJpaRepository orderDataJpaRepository,
            final OrderTransactionalOutboxRepository orderTransactionalOutboxRepository,
            final ObjectMapper objectMapper,
            final Flyway flyway) {
        super(objectMapper, flyway);
        this.orderDataJpaRepository = orderDataJpaRepository;
        this.orderTransactionalOutboxRepository = orderTransactionalOutboxRepository;
    }

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("com.github.caiquetgr.orders.fixture");
    }

    @SneakyThrows
    @Test
    void shouldCreateOrderAndReturn200() {
        final OrderRequest orderRequest = gimmeValidOrderRequest();

        given()
                .body(objectMapper.writeValueAsString(orderRequest))
                .contentType(ContentType.JSON)
                .when()
                .post(getHost() + "/v1/orders")
                .then()
                .statusCode(201)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("jsonschema/orders-post-v1.json"));

        final Iterable<OrderEntity> orderEntities = orderDataJpaRepository.findAll();

        assertThat(orderEntities, OrderEntityIntegrationTestAssert.class)
                .singleElement()
                .isEqualToOrderRequest(orderRequest);

        assertThat(orderTransactionalOutboxRepository.findAll())
                .singleElement()
                .extracting(OrderTransactionalOutboxEntity::getOrder)
                .isEqualTo(objectMapper.writeValueAsString(orderEntities.iterator().next()));
    }

    private OrderRequest gimmeValidOrderRequest() {
        return Fixture.from(OrderRequest.class).gimme(VALID_IT);
    }

}

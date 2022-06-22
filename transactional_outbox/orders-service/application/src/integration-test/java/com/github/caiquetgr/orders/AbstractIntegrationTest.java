package com.github.caiquetgr.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public abstract class AbstractIntegrationTest {

    protected final ObjectMapper objectMapper;
    protected final Flyway flyway;

    @LocalServerPort
    protected Integer localServerPort;

    public AbstractIntegrationTest(final ObjectMapper objectMapper, final Flyway flyway) {
        this.objectMapper = objectMapper;
        this.flyway = flyway;
    }

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:13")
                .withDatabaseName("orders_db")
                .withUsername("orders_user")
                .withPassword("orders_password");

        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
    }

    @BeforeEach
    void cleanDatabaseBeforeEach() {
        flyway.clean();
        flyway.migrate();
        flyway.validate();
    }

    protected String getHost() {
        return "http://localhost:" + localServerPort;
    }
}

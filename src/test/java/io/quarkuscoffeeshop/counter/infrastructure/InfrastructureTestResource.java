package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;

/**
 * Starts Docker Containers running Kafka and PostgreSQL for use in integration tests
 */
public class InfrastructureTestResource implements QuarkusTestResourceLifecycleManager {

    final KafkaContainer KAFKA = new KafkaContainer();
    final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>();

    @Override
    public Map<String, String> start() {
        KAFKA.start();
        System.setProperty("KAFKA_BOOTSTRAP_URLS", KAFKA.getBootstrapServers());

        POSTGRES.withInitScript("postgresql-init.sql");
        System.setProperty("POSTGRES_JDBC_URL", POSTGRES.getJdbcUrl());
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("KAFKA_BOOTSTRAP_URLS");
        System.clearProperty("POSTGRES_JDBC_URL");
        KAFKA.close();
        POSTGRES.close();
    }
}

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

    private KafkaContainer kafkaContainer;
//    private PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

    @Override
    public Map<String, String> start() {
        kafkaContainer = new KafkaContainer("confluentinc/cp-kafka:5.4.3");
        kafkaContainer.start();
        System.setProperty("KAFKA_BOOTSTRAP_URLS", kafkaContainer.getBootstrapServers());

/*
        postgreSQLContainer = new PostgreSQLContainer<>();
        postgreSQLContainer.withInitScript("postgresql-init.sql");
        System.setProperty("POSTGRES_JDBC_URL", postgreSQLContainer.getJdbcUrl());
*/
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("KAFKA_BOOTSTRAP_URLS");
        System.clearProperty("POSTGRES_JDBC_URL");
        kafkaContainer.close();
//        postgreSQLContainer.close();
    }
}

package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    private KafkaContainer kafkaContainer;

    @Override
    public Map<String, String> start() {

        this.kafkaContainer = new KafkaContainer();
        this.kafkaContainer.start();

        return configurationParameters();
    }

    private Map<String, String> configurationParameters() {

        final Map<String, String> conf = new HashMap<>();
        conf.put("mp.messaging.outgoing.delivery.bootstrap.servers",  kafkaContainer.getContainerIpAddress() + ":" + kafkaContainer.getMappedPort(KafkaContainer.KAFKA_PORT));
        return conf;
    }

    @Override
    public void stop() {

    }
}

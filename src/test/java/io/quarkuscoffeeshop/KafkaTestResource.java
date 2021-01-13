package io.quarkuscoffeeshop;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {

        return null;
    }

    @Override
    public void stop() {
    }

    @BeforeAll
    public static void switchMyChannels() {
        InMemoryConnector.switchIncomingChannelsToInMemory("orders-in");
        InMemoryConnector.switchIncomingChannelsToInMemory("orders-up");
        InMemoryConnector.switchOutgoingChannelsToInMemory("web-updates");
    }

    // 2. Don't forget to reset the channel after the tests:
    @AfterAll
    public static void revertMyChannels() {
        InMemoryConnector.clear();
    }

}

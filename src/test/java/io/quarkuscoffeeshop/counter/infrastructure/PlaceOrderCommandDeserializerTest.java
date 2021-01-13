package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceOrderCommandDeserializerTest {

    @Test
    public void testDeserializingPlaceOrderCommand() {
        String json = "{\"id\":\"0d405182-02b4-48b6-b686-270c65b0823e\",\"orderSource\":\"WEB\",\"location\":\"ATLANTA\",\"loyaltyMemberId\":\"foo@bar.com\",\"baristaLineItems\":[{\"item\":\"COFFEE_WITH_ROOM\",\"name\":\"Goose\",\"orderId\":\"0d405182-02b4-48b6-b686-270c65b0823e\"}],\"kitchenLineItems\":[]}";

        PlaceOrderCommandDeserializer placeOrderCommandDeserializer = new PlaceOrderCommandDeserializer();
        try {

            PlaceOrderCommand placeOrderCommand = placeOrderCommandDeserializer.deserialize(json, json.getBytes(StandardCharsets.UTF_8));
            assertNotNull(placeOrderCommand);
            assertEquals("a2a912e9-47cc-425f-8a26-81a6ad3ff4e6", placeOrderCommand.getId(), "id should match");

        } catch (Exception e) {
            assertNull(e);
        }
    }
}

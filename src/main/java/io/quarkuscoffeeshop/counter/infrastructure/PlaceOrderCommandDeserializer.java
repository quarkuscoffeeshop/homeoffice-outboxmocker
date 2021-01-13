package io.quarkuscoffeeshop.counter.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Location;
import io.quarkuscoffeeshop.counter.domain.OrderSource;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Custom Jackson deserializer for PlaceOrderCommands
 */
public class PlaceOrderCommandDeserializer extends ObjectMapperDeserializer<PlaceOrderCommand> {

    public PlaceOrderCommandDeserializer() {
        super(PlaceOrderCommand.class);
    }

/*
    @Override
    public PlaceOrderCommand deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

*/
/*
        @JsonProperty("id") final String id,
        @JsonProperty("orderSource") final OrderSource orderSource,
        @JsonProperty("location") final Location location,
        @JsonProperty("rewardsId") final String loyaltyMemberId,
        @JsonProperty("baristaItems") Optional<List<LineItem>> baristaLineItems,
        @JsonProperty("kitchenItems") Optional<List<LineItem>> kitchenLineItems) {
*//*


        JsonNode node = jp.getCodec().readTree(jp);
        String id = node.get("id").asText();
        OrderSource orderSource =
        String itemName = node.get("itemName").asText();
        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

    }
*/
}

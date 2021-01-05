package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Location;
import io.quarkuscoffeeshop.counter.domain.OrderSource;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrderServiceTest {

    Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @Inject
    OrderService orderService;

    @Inject
    BaristaStream baristaStream;

    @Test
    public void testPlacingOrder() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                new ArrayList<>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }},
                null);

        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(baristaStream.getOrderTickets().size(), 1, "1 ticket should have been delivered to the 'barista' stream");
        logger.info("Ticket received {}", baristaStream.getOrderTickets().get(0));
        System.out.println(baristaStream.getOrderTickets().get(0).toString());
    }
}

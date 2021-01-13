package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkuscoffeeshop.counter.domain.*;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

//@QuarkusMain
public class MockerService implements QuarkusApplication {

    final Logger logger = LoggerFactory.getLogger(MockerService.class);

    @Inject
    OrderService orderService;

    private boolean running = true;

    private Runnable mockOrders = () -> {

        logger.debug("Mocker now running");

        while (running) {
            try {
                Thread.sleep(3000);
                int orders = new Random().nextInt(33);
                String loyaltyMemberId = null;
                if(orders %3 == 0){
                    loyaltyMemberId = UUID.randomUUID().toString();
                }
                if (orders %2 == 0) {
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                            UUID.randomUUID().toString(),
                            OrderSource.COUNTER,
                            Location.ATLANTA,
                            loyaltyMemberId,
                            Optional.of(
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }}),
                            null);
                    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
                    orderService.onOrderIn(placeOrderCommand);
                }else{
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                            UUID.randomUUID().toString(),
                            OrderSource.WEB,
                            Location.CHARLOTTE,
                            loyaltyMemberId,
                            Optional.of(
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }}),
                            Optional.of(
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.CAKEPOP, "John"));
                            }}));
                    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
                    orderService.onOrderIn(placeOrderCommand);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int run(String... args) throws Exception {
        this.running = true;
        logger.info("starting");
        mockOrders.run();
        return 10;
    }
}

package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkuscoffeeshop.counter.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@QuarkusMain
public class MockerService implements QuarkusApplication {

    final Logger logger = LoggerFactory.getLogger(MockerService.class);

    private boolean running = true;

    private Runnable mockOrders = () -> {

        logger.debug("Mocker now running");

        while (running) {
            try {
                Thread.sleep(3000);
                int orders = new Random().nextInt(9);
                if (orders %2 > 1) {
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                            UUID.randomUUID().toString(),
                            "WEB",
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }},
                            null);
                    OrderEvent orderEvent = Order.process(placeOrderCommand);
                    orderEvent.getOrder().persist();
                }else{
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(UUID.randomUUID().toString(),
                            "WEB",
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }},
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.CAKEPOP, "John"));
                            }});
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

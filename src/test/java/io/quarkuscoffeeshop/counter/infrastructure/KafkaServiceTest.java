package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.concurrent.CompletionStage;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest @Transactional
public class KafkaServiceTest {

    @InjectSpy
    OrderService orderService;

    @Channel("orders-in")
    Emitter ordersInEmitter;

    @Test
    public void testOrderIn() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleBlackCoffee();
        ordersInEmitter.send(placeOrderCommand);
        Mockito.verify(orderService, Mockito.times(1)).onPlaceOrderCommand(any(PlaceOrderCommand.class));
    }
}

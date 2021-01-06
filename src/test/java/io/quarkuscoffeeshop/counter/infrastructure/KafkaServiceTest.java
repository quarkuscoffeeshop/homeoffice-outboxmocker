package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.valueobjects.ValueObjectMocker;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest @Transactional
public class KafkaServiceTest {

    @InjectSpy
    OrderService orderService;

    @Channel("orders-in")
    Emitter ordersInEmitter;

    @Channel("orders-up")
    Emitter ordersUpEmitter;

    @Test
    public void testOrderIn() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleBlackCoffee();
        ordersInEmitter.send(placeOrderCommand);
        Mockito.verify(orderService, Mockito.times(1)).onPlaceOrderCommand(any(PlaceOrderCommand.class));
    }

    @Test
    public void testOrderUp() {

        OrderTicket orderTicket = ValueObjectMocker.blackCoffee();
        ordersUpEmitter.send(orderTicket);
        Mockito.verify(orderService, Mockito.times(1)).onOrderUp(any(OrderTicket.class));
    }
}

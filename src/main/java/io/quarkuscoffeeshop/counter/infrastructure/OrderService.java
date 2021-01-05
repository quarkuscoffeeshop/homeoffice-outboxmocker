package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

  Logger logger = LoggerFactory.getLogger(OrderService.class);

  @Inject
  Event<ExportedEvent<?, ?>> event;

  @Channel("barista")
  Emitter<OrderTicket> baristaEmitter;

/*
  @Channel("kitchen")
  Emitter<OrderTicket> kitchenEmitter;

  @Channel("orders")
  Emitter<String> ordersEmitter;
*/


  @Transactional
  public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

    logger.debug("onPlaceOrderCommand {}", placeOrderCommand);
    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
    orderEventResult.getOrder().persist();
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
      event.fire(exportedEvent);
    });
    if(!orderEventResult.getBaristaTickets().isEmpty()){
      orderEventResult.getBaristaTickets().forEach(baristaTicket -> {
        baristaEmitter.send(baristaTicket);
      });
    }
/*
    if (!orderEventResult.getKitchenTickets().isEmpty()) {
      orderEventResult.getKitchenTickets().forEach(kitchenTicket -> {
        kitchenEmitter.send(kitchenTicket);
      });
    }
*/
  }
}

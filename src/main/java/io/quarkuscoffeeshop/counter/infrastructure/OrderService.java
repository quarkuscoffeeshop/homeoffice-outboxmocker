package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
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
  Emitter<String> baristaEmitter;

  @Channel("barista")
  Emitter<String> kitchenEmitter;

  @Channel("orders")
  Emitter<String> ordersEmitter;


  @Transactional
  public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

    logger.debug("onPlaceOrderCommand {}", placeOrderCommand);
    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
    orderEventResult.getOrder().persist();
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
      event.fire(exportedEvent);
    });
  }
}

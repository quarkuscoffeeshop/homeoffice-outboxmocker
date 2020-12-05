package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.*;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Path("/json")
@Produces(MediaType.APPLICATION_JSON)
public class JsonResource {

  Logger logger = LoggerFactory.getLogger(JsonResource.class);

  @GET
  @Path("/PlaceOrderCommand")
  public Response placeOrderCommand() {

    Order order = new Order();
    order.setOrderId(UUID.randomUUID().toString());

    return Response.ok(new PlaceOrderCommand(
      OrderSource.WEB,
      Location.ATLANTA,
      null,
      new ArrayList<LineItem>(){{
        add(new LineItem(
          Item.CAPPUCCINO,
          "Goofy",
          order
        ));
      }},
      new ArrayList< LineItem >(){{
        add(new LineItem(
          Item.CAKEPOP,
          "Goofy",
          order
        ));
      }}
    )).build();
  }

  @GET
  @Path("/OrderTicket")
  public Response getOrderTicket() {
    OrderTicket orderTicket = new OrderTicket(
      UUID.randomUUID().toString(),
      UUID.randomUUID().toString(),
      Item.COFFEE_BLACK,
      "Eric");

    return Response.ok(orderTicket).build();
  }
}

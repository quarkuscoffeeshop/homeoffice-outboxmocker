package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Order;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    @Path("/mock")
    public Order getMockOrder() {

        return mockOrder();
    }

    @GET
    @Path("/{id}")
    public Order findOrderById(@PathParam("id") String id) {

        return Order.findById(id);
    }

    @POST
    @Transactional
    public Order createOrder(final Order orderToPersist) {
        Order order = new Order();
        order.setOrderSource(orderToPersist.getOrderSource());
        order.setTimestamp(orderToPersist.getTimestamp());
        if(orderToPersist.getBaristaLineItems().isPresent()){
            order.setBaristaLineItems(orderToPersist.getBaristaLineItems().get());
        }
        if (orderToPersist.getKitchenLineItems().isPresent()) {
            order.setKitchenLineItems(orderToPersist.getKitchenLineItems().get());
        }
        order.persist();
        return order;
    }

    private Order mockOrder() {

        Order order = new Order();
        order.setBaristaLineItems(mockBaristaItems(order));
        return order;
    }

    private List<LineItem> mockBaristaItems(Order order) {
        return new ArrayList<>(){{
            add(new LineItem(Item.ESPRESSO, "Lemmy", order));
        }};
    }

}

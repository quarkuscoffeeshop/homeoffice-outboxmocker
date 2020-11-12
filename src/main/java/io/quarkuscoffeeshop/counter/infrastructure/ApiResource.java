package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.Receipt;
import io.quarkuscoffeeshop.counter.domain.ReceiptLineItem;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    @Path("/mock")
    public Receipt getMockReceipt() {

        return mockReceipt();
    }

    @GET
    @Path("/{id}")
    public Receipt findReceiptById(@PathParam("id") String id) {

        return Receipt.findById(id);
    }

    @POST
    @Transactional
    public Receipt createReceipt(Receipt receipt) {
        receipt.persist();
        return receipt;
    }

    private Receipt mockReceipt() {

        Receipt receipt = new Receipt();
        receipt.setOrderId(UUID.randomUUID().toString());
        receipt.setTotal(BigDecimal.valueOf(3.50));
        receipt.setLineItems(mockBaristaItems(receipt));
        return receipt;
    }

    private List<ReceiptLineItem> mockBaristaItems(Receipt receipt) {
        return new ArrayList<>(){{
            add(new ReceiptLineItem(receipt, Item.ESPRESSO, "Lemmy"));
        }};
    }

}
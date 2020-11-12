package io.quarkuscoffeeshop.counter.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class PlaceOrderCommand {

    private String id;

    private String orderSource;

    private List<LineItem> baristaLineItems;

    private List<LineItem> kitchenLineItems;

    private Instant timestamp;

    public PlaceOrderCommand() {
    }

    public PlaceOrderCommand(String id, String orderSource, List<LineItem> baristaLineItems, List<LineItem> kitchenLineItems) {
        this.id = id;
        this.orderSource = orderSource;
        this.baristaLineItems = baristaLineItems;
        this.kitchenLineItems = kitchenLineItems;
        this.timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlaceOrderCommand.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orderSource='" + orderSource + "'")
                .add("baristaLineItems=" + baristaLineItems)
                .add("kitchenLineItems=" + kitchenLineItems)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(orderSource, that.orderSource) &&
                Objects.equals(baristaLineItems, that.baristaLineItems) &&
                Objects.equals(kitchenLineItems, that.kitchenLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSource, baristaLineItems.size(), kitchenLineItems.size());
    }

    public Optional<List<LineItem>> getBaristaLineItems() {
        return Optional.ofNullable(baristaLineItems);
    }

    public Optional<List<LineItem>> getKitchenLineItems() {
        return Optional.ofNullable(kitchenLineItems);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public void setBaristaLineItems(List<LineItem> baristaLineItems) {
        this.baristaLineItems = baristaLineItems;
    }

    public void setKitchenLineItems(List<LineItem> kitchenLineItems) {
        this.kitchenLineItems = kitchenLineItems;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

package io.quarkuscoffeeshop.counter.commands;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Location;
import io.quarkuscoffeeshop.counter.domain.OrderSource;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class CommandMocker {

    /**
     * Returns a PlaceOrderCommand with the following parameters: OrderSource.WEB, Location.ATLANTA, Item.COFFEE_BLACK
     *
     * @return
     * @see PlaceOrderCommand
     */
    public static PlaceOrderCommand placeOrderCommandSingleBlackCoffee() {
        return new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                Optional.of(new ArrayList() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }}),
                null);
    }

    /**
     * Returns a PlaceOrderCommand with the following parameters: OrderSource.WEB, Location.ATLANTA, Item.CROISSANT
     *
     * @return
     * @see PlaceOrderCommand
     */
    public static PlaceOrderCommand placeOrderCommandSingleCroissant() {
        return new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                null,
                Optional.of(new ArrayList() {{
                    add(new LineItem(Item.CROISSANT, "Paul"));
                }}));
    }

    public static PlaceOrderCommand placeOrderCommandBlackCoffeeAndCroissant() {
        return new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                Optional.of(new ArrayList() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }}),
                Optional.of(new ArrayList() {{
                    add(new LineItem(Item.CROISSANT, "Paul"));
                }}));
    }

}

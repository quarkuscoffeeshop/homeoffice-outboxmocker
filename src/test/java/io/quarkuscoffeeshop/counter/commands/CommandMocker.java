package io.quarkuscoffeeshop.counter.commands;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Location;
import io.quarkuscoffeeshop.counter.domain.OrderSource;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;

import java.util.ArrayList;

public class CommandMocker {

    /**
     * Returns a PlaceOrderCommand with the following parameters: OrderSource.WEB, Location.ATLANTA, Item.COFFEE_BLACK
     *
     * @return
     * @see PlaceOrderCommand
     */
    public static PlaceOrderCommand placeOrderCommandSingleBlackCoffee() {
        return new PlaceOrderCommand(
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                new ArrayList<>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }},
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
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                null,
                new ArrayList<>() {{
                    add(new LineItem(Item.CROISSANT, "John"));
                }});
    }

    public static PlaceOrderCommand placeOrderCommandBlackCoffeeAndCroissant() {
        return new PlaceOrderCommand(
                OrderSource.WEB,
                Location.ATLANTA,
                null,
                new ArrayList<>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }},
                new ArrayList<>() {{
                    add(new LineItem(Item.CROISSANT, "John"));
                }});
    }

}

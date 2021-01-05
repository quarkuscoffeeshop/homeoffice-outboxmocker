package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class EmptyQuarkusTest {

    @Inject
    OrderService orderService;

    @Test
    public void testTest() {
        System.out.println("test");
        assertNotNull(orderService);
        assertTrue(false);
    }
}

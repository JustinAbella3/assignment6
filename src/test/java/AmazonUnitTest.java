package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonUnitTest {

    private Amazon amazon;
    private TestShoppingCart testCart;
    private List<PriceRule> testRules;

    private class TestShoppingCart implements ShoppingCart {
        private List<Item> items = new ArrayList<>();

        @Override
        public List<Item> getItems() {
            return items;
        }

        @Override
        public void add(Item item) {
            items.add(item);
        }

        @Override
        public int numberOfItems() {
            return items.size();
        }
    }

    private class TestPriceRule implements PriceRule {
        private final double priceToReturn;

        public TestPriceRule(double priceToReturn) {
            this.priceToReturn = priceToReturn;
        }

        @Override
        public double priceToAggregate(List<Item> items) {
            return priceToReturn;
        }
    }

    @BeforeEach
    void setUp() {
        testCart = new TestShoppingCart();
        testRules = new ArrayList<>();
        amazon = new Amazon(testCart, testRules);
    }

    @Test
    @DisplayName("specification-based")
    void calculateReturnsZeroWithNoRules() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        testCart.add(laptop);

        double result = amazon.calculate();

        assertEquals(0.0, result, 0.001, "Calculate should return 0 when there are no rules");
    }

    @Test
    @DisplayName("specification-based")
    void calculateReturnsSumOfAllRulePrices() {
        testRules.add(new TestPriceRule(10.0));
        testRules.add(new TestPriceRule(20.0));
        testRules.add(new TestPriceRule(30.0));

        double result = amazon.calculate();

        assertEquals(60.0, result, 0.001, "Calculate should return the sum of all rule prices");
    }

    @Test
    @DisplayName("specification-based")
    void addToCartAddsItemToShoppingCart() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);

        amazon.addToCart(laptop);

        assertEquals(1, testCart.numberOfItems(), "Cart should contain 1 item");
        assertTrue(testCart.getItems().contains(laptop), "Cart should contain the added item");
    }

    @Test
    @DisplayName("structural-based")
    void calculateCallsPriceToAggregateOnAllRules() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        Item book = new Item(ItemType.OTHER, "Design Patterns", 1, 40.0);
        testCart.add(laptop);
        testCart.add(book);

        testRules.add(new PriceRule() {
            @Override
            public double priceToAggregate(List<Item> items) {
                return items.size() * 5.0;
            }
        });

        testRules.add(new PriceRule() {
            @Override
            public double priceToAggregate(List<Item> items) {
                double total = 0;
                for (Item item : items) {
                    total += item.getPricePerUnit() * item.getQuantity();
                }
                return total * 0.1;
            }
        });

        double result = amazon.calculate();

        assertEquals(134.0, result, 0.001, "Calculate should return the correct aggregated price");
    }

    @Test
    @DisplayName("structural-based")
    void calculateReturnsCorrectSumWithMultipleItems() {
        testCart.add(new Item(ItemType.ELECTRONIC, "iPhone", 1, 800.0));
        testCart.add(new Item(ItemType.ELECTRONIC, "Headphones", 1, 150.0));
        testCart.add(new Item(ItemType.OTHER, "Coffee Mug", 2, 7.5));

        testRules.add(new TestPriceRule(5.0));

        testRules.add(new PriceRule() {
            @Override
            public double priceToAggregate(List<Item> items) {
                return items.size() * 2.0;
            }
        });

        double result = amazon.calculate();

        assertEquals(11.0, result, 0.001, "Calculate should return the correct sum with multiple items");
    }

    @Test
    @DisplayName("structural-based")
    void calculateUsesItemsFromShoppingCart() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        testCart.add(laptop);

        testRules.add(new PriceRule() {
            @Override
            public double priceToAggregate(List<Item> items) {
                return items.size() * 3.0;
            }
        });

        double result = amazon.calculate();

        assertEquals(3.0, result, 0.001, "Calculate should use the items from the shopping cart");

        amazon.addToCart(new Item(ItemType.OTHER, "Design Patterns", 1, 40.0));
        result = amazon.calculate();
        assertEquals(6.0, result, 0.001, "Calculate should reflect changes to the shopping cart");
    }

    @Test
    @DisplayName("structural-based")
    void addToCartMultipleItemsAreAddedCorrectly() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        Item phone = new Item(ItemType.ELECTRONIC, "iPhone", 1, 800.0);
        Item book = new Item(ItemType.OTHER, "Design Patterns", 1, 40.0);

        amazon.addToCart(laptop);
        amazon.addToCart(phone);
        amazon.addToCart(book);

        assertEquals(3, testCart.numberOfItems(), "Cart should contain 3 items");
        assertTrue(testCart.getItems().contains(laptop), "Cart should contain the laptop");
        assertTrue(testCart.getItems().contains(phone), "Cart should contain the phone");
        assertTrue(testCart.getItems().contains(book), "Cart should contain the book");
    }
}
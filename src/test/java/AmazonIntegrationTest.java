package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {

    private Amazon amazon;
    private RealShoppingCart shoppingCart;
    private List<PriceRule> priceRules;

    @BeforeEach
    void setUp() {
        resetDatabase();
        shoppingCart = new RealShoppingCart();
        priceRules = createPriceRules();
        amazon = new Amazon(shoppingCart, priceRules);
    }

    private void resetDatabase() {
    }

    private List<PriceRule> createPriceRules() {
        List<PriceRule> rules = new ArrayList<>();
        rules.add(new BasePriceRule());
        rules.add(new ExtraCostForElectronics());
        return rules;
    }

    @Test
    @DisplayName("specification-based")
    void calculateReturnsCorrectPriceForNonElectronicItem() {
        Item book = new Item(ItemType.OTHER, "Design Patterns", 1, 40.0);
        amazon.addToCart(book);

        double result = amazon.calculate();

        assertEquals(40.0, result, 0.001,
                "Calculate should return the regular price for non-electronic items");
    }

    @Test
    @DisplayName("specification-based")
    void calculateAddsExtraCostForElectronicItems() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        amazon.addToCart(laptop);

        double result = amazon.calculate();

        assertEquals(1207.50, result, 0.001,
                "Calculate should add $7.50 for electronic items");
    }

    @Test
    @DisplayName("specification-based")
    void calculateHandlesMixedItemTypes() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        Item book = new Item(ItemType.OTHER, "Design Patterns", 1, 40.0);

        amazon.addToCart(laptop);
        amazon.addToCart(book);

        double result = amazon.calculate();

        assertEquals(1247.50, result, 0.001,
                "Calculate should add base price plus $7.50 for mixed items with electronics");
    }

    @Test
    @DisplayName("structural-based")
    void extraCostAppliesOnlyOnceRegardlessOfNumberOfElectronics() {
        Item laptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        Item phone = new Item(ItemType.ELECTRONIC, "iPhone", 1, 800.0);

        amazon.addToCart(laptop);
        double resultWithOneElectronic = amazon.calculate();

        amazon.addToCart(phone);
        double resultWithTwoElectronics = amazon.calculate();

        assertEquals(1207.50, resultWithOneElectronic, 0.001,
                "Calculate should add $7.50 for one electronic item");

        assertEquals(2007.50, resultWithTwoElectronics, 0.001,
                "Calculate should add $7.50 only once for multiple electronic items");
    }

    @Test
    @DisplayName("structural-based")
    void extraCostDoesNotApplyWhenNoElectronicsPresent() {
        Item book1 = new Item(ItemType.OTHER, "Design Patterns", 1, 40.0);
        Item book2 = new Item(ItemType.OTHER, "Clean Code", 1, 35.0);

        amazon.addToCart(book1);
        amazon.addToCart(book2);

        double result = amazon.calculate();

        assertEquals(75.0, result, 0.001,
                "Calculate should not add extra cost when no electronics are present");
    }

    @Test
    @DisplayName("structural-based")
    void multipleQuantitiesAreHandledCorrectly() {
        Item singleLaptop = new Item(ItemType.ELECTRONIC, "MacBook Pro", 1, 1200.0);
        Item multipleBooks = new Item(ItemType.OTHER, "Design Patterns", 3, 40.0);

        amazon.addToCart(singleLaptop);
        amazon.addToCart(multipleBooks);

        double result = amazon.calculate();

        assertEquals(1327.50, result, 0.001,
                "Calculate should handle multiple quantities correctly");
    }

    @Test
    @DisplayName("structural-based")
    void multipleRulesAreAppliedInCorrectOrder() {
        List<PriceRule> orderedRules = new ArrayList<>();
        TestPriceRule rule1 = new TestPriceRule(10.0);
        TestPriceRule rule2 = new TestPriceRule(20.0);
        orderedRules.add(rule1);
        orderedRules.add(rule2);

        Amazon testAmazon = new Amazon(shoppingCart, orderedRules);
        testAmazon.addToCart(new Item(ItemType.OTHER, "Test Item", 1, 100.0));

        double result = testAmazon.calculate();

        assertEquals(30.0, result, 0.001,
                "Rules should be applied in the order they are added");
    }

    private static class BasePriceRule implements PriceRule {
        @Override
        public double priceToAggregate(List<Item> cart) {
            double total = 0;
            for (Item item : cart) {
                total += item.getPricePerUnit() * item.getQuantity();
            }
            return total;
        }
    }

    private static class TestPriceRule implements PriceRule {
        private final double amount;

        public TestPriceRule(double amount) {
            this.amount = amount;
        }

        @Override
        public double priceToAggregate(List<Item> cart) {
            return amount;
        }
    }

    private static class RealShoppingCart implements ShoppingCart {
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
}
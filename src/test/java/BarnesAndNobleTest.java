import org.example.Barnes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class BarnesAndNobleTest {

    private BarnesAndNoble barnesAndNoble;

    // Test doubles defined inside test class
    private class TestBookDatabase implements BookDatabase {
        @Override
        public Book findByISBN(String ISBN) {
            if ("1234567890".equals(ISBN)) {
                return new Book("1234567890", 20, 10);
            } else if ("0987654321".equals(ISBN)) {
                return new Book("0987654321", 30, 5);
            }
            return null;
        }
    }

    private class TestBuyBookProcess implements BuyBookProcess {
        @Override
        public void buyBook(Book book, int quantity) {
            // Test implementation - just a placeholder for testing
        }
    }

    @BeforeEach
    void setUp() {
        BookDatabase bookDatabase = new TestBookDatabase();
        BuyBookProcess process = new TestBuyBookProcess();
        barnesAndNoble = new BarnesAndNoble(bookDatabase, process);
    }

    @Test
    @DisplayName("specification-based")
    void getPriceForCartReturnsCorrectTotalForAvailableBooks() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();
        order.put("1234567890", 2); // 2 copies of Test Book 1
        order.put("0987654321", 1); // 1 copy of Test Book 2

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(70.0, result.getTotalPrice(), 0.001,
                "Total price should be (2 * $20) + (1 * $30) = $70");
        Assertions.assertEquals(0, result.getUnavailable().size(),
                "There should be no unavailable books");
    }

    @Test
    @DisplayName("specification-based")
    void getPriceForCartHandlesPartiallyAvailableBooks() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();
        order.put("1234567890", 15); // Request more copies than available (10)

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(200.0, result.getTotalPrice(), 0.001,
                "Total price should be 10 * $20 = $200 (only available quantity)");
        Assertions.assertEquals(1, result.getUnavailable().size(),
                "There should be one unavailable book entry");
    }

    @Test
    @DisplayName("specification-based")
    void getPriceForCartReturnsNullForNullOrder() {
        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(null);

        // Assert
        Assertions.assertNull(result, "Should return null for null order");
    }

    @Test
    @DisplayName("structural-based")
    void retrieveBookAddsCorrectAmountToTotalPrice() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();
        order.put("1234567890", 3); // 3 copies of Test Book 1 at $20 each

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(60.0, result.getTotalPrice(), 0.001,
                "Total price should be 3 * $20 = $60");
    }

    @Test
    @DisplayName("structural-based")
    void retrieveBookHandlesMultipleBooks() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();
        order.put("1234567890", 2); // 2 copies of Test Book 1
        order.put("0987654321", 3); // 3 copies of Test Book 2

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(130.0, result.getTotalPrice(), 0.001,
                "Total price should be (2 * $20) + (3 * $30) = $130");
    }

    @Test
    @DisplayName("structural-based")
    void retrieveBookHandlesMultipleUnavailableBooks() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();
        order.put("1234567890", 12); // 12 copies of Test Book 1 (only 10 available)
        order.put("0987654321", 8);  // 8 copies of Test Book 2 (only 5 available)

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(350.0, result.getTotalPrice(), 0.001,
                "Total price should be (10 * $20) + (5 * $30) = $350 (only available quantities)");
        Assertions.assertEquals(2, result.getUnavailable().size(),
                "There should be two unavailable book entries");
    }

    @Test
    @DisplayName("structural-based")
    void retrieveBookHandlesEmptyOrder() {
        // Arrange
        Map<String, Integer> order = new HashMap<>();

        // Act
        PurchaseSummary result = barnesAndNoble.getPriceForCart(order);

        // Assert
        Assertions.assertEquals(0.0, result.getTotalPrice(), 0.001,
                "Total price should be 0 for empty order");
        Assertions.assertEquals(0, result.getUnavailable().size(),
                "There should be no unavailable books");
    }
}
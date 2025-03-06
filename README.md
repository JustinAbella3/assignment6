Assignment 6
Project Overview

Part 1: BarnesAndNoble Testing

Specification-based Tests

getPriceForCartReturnsCorrectTotalForAvailableBooks: Verifies that the correct total price is calculated when all requested books are available

getPriceForCartHandlesPartiallyAvailableBooks: Tests the behavior when a customer requests more copies than are available in the inventory

getPriceForCartReturnsNullForNullOrder: Validates that the method properly handles null inputs

Structural-based Tests

retrieveBookAddsCorrectAmountToTotalPrice: Ensures that the price calculation logic works properly for a single book

retrieveBookHandlesMultipleBooks: Tests that multiple books in an order are processed correctly

retrieveBookHandlesMultipleUnavailableBooks: Verifies that multiple books with partial availability are handled correctly

retrieveBookHandlesEmptyOrder: Tests the edge case of an empty order
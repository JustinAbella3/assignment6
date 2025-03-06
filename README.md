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

Part 3: Amazon Testing

Unit Tests

Specification-based Tests

calculateReturnsZeroWithNoRules: Verifies that the calculate method returns zero when no pricing rules are defined

calculateReturnsSumOfAllRulePrices: Checks that the calculate method correctly sums prices from multiple pricing rules

addToCartAddsItemToShoppingCart: Confirms that an item can be successfully added to the shopping cart

Structural-based Tests

calculateCallsPriceToAggregateOnAllRules: Ensures that price aggregation rules are applied correctly to different items in the cart

calculateReturnsCorrectSumWithMultipleItems: Validates that the calculation method works correctly with multiple items and different pricing rules

calculateUsesItemsFromShoppingCart: Verifies that the calculate method dynamically uses current items in the shopping cart

addToCartMultipleItemsAreAddedCorrectly: Checks that multiple items of different types can be added to the cart simultaneously

Integration Tests

Part 1: Amazon Integration Testing

Specification-based Tests

calculateReturnsCorrectPriceForNonElectronicItem: Verifies that non-electronic items are priced at their base price without additional costs

calculateAddsExtraCostForElectronicItems: Checks that electronic items have an additional fixed cost applied

calculateHandlesMixedItemTypes: Confirms that mixed cart items (electronic and non-electronic) are correctly priced

Structural-based Tests

extraCostAppliesOnlyOnceRegardlessOfNumberOfElectronics: Ensures that the extra electronic item cost is applied consistently, regardless of the number of electronic items

extraCostDoesNotApplyWhenNoElectronicsPresent: Validates that no extra cost is added when the cart contains only non-electronic items

multipleQuantitiesAreHandledCorrectly: Checks that items with multiple quantities are priced accurately

multipleRulesAreAppliedInCorrectOrder: Verifies that pricing rules are applied in the sequence they are added to the cart

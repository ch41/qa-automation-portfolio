package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.InventoryPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("Checkout")
class CheckoutTest extends BaseWebTest {

    private static final TestData.Customer CUSTOMER = TestData.checkoutCustomer();
    private static final String PAYMENT_INFO = TestData.checkoutPaymentInfo();
    private static final String SHIPPING_INFO = TestData.checkoutShippingInfo();

    private final LoginPage loginPage = new LoginPage();
    private InventoryPage inventoryPage;

    @BeforeEach
    void loginAsStandardUser() {
        TestData.User user = TestData.webUser("standard");

        inventoryPage = loginPage.openLoginPage()
                .loginAs(user.username(), user.password())
                .shouldBeOpened();
    }

    @Tag("smoke")
    @Test
    @Story("Happy path")
    void shouldCompleteCheckoutWithSingleProduct() {
        TestData.Order order = TestData.checkoutOrder("singleProduct");
        TestData.Product product = order.items().get(0);

        inventoryPage.shouldBeOpened()
                .shouldHaveAddToCartButton(product.name())
                .addProductToCart(product.name())
                .shouldHaveRemoveButton(product.name());
        inventoryPage.cartBadge().shouldHaveCartBadge(1);

        inventoryPage.openCart()
                .shouldBeOpened()
                .shouldContainItem(product.name())
                .checkout()
                .shouldBeOpened()
                .fillCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), CUSTOMER.postalCode())
                .shouldHaveCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), CUSTOMER.postalCode())
                .continueToOverview()
                .shouldBeOpened()
                .shouldContainItem(product.name())
                .shouldContainItemPrice(product.name(), product.price())
                .shouldHaveItemQuantity(product.name(), 1)
                .shouldHaveOrderTotals(order.subtotal(), order.tax(), order.total())
                .shouldHavePaymentInfo(PAYMENT_INFO)
                .shouldHaveShippingInfo(SHIPPING_INFO)
                .finish()
                .shouldBeOpened()
                .shouldShowConfirmation()
                .backHome()
                .shouldBeOpened()
                .cartBadge()
                .shouldNotHaveCartBadge();
    }

    @Test
    @Story("Happy path with multiple products")
    void shouldCompleteCheckoutWithMultipleProducts() {
        TestData.Order order = TestData.checkoutOrder("multipleProducts");
        TestData.Product backpack = order.items().get(0);
        TestData.Product bikeLight = order.items().get(1);

        inventoryPage.addProductToCart(backpack.name())
                .shouldHaveRemoveButton(backpack.name())
                .addProductToCart(bikeLight.name())
                .shouldHaveRemoveButton(bikeLight.name());
        inventoryPage.cartBadge().shouldHaveCartBadge(order.items().size());

        inventoryPage.openCart()
                .shouldBeOpened()
                .shouldHaveItemsCount(order.items().size())
                .shouldContainItem(backpack.name())
                .shouldContainItem(bikeLight.name())
                .checkout()
                .shouldBeOpened()
                .fillCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), CUSTOMER.postalCode())
                .continueToOverview()
                .shouldBeOpened()
                .shouldContainItem(backpack.name())
                .shouldContainItemPrice(backpack.name(), backpack.price())
                .shouldContainItem(bikeLight.name())
                .shouldContainItemPrice(bikeLight.name(), bikeLight.price())
                .shouldHaveOrderTotals(order.subtotal(), order.tax(), order.total())
                .finish()
                .shouldBeOpened()
                .shouldShowConfirmation()
                .backHome()
                .shouldBeOpened()
                .cartBadge()
                .shouldNotHaveCartBadge();
    }
}

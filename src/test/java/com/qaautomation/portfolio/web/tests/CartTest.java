package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.CartPage;
import com.qaautomation.portfolio.web.pages.InventoryPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("Cart")
class CartTest extends BaseWebTest {

    private static final TestData.Product BACKPACK = TestData.product("backpack");
    private static final TestData.Product BIKE_LIGHT = TestData.product("bikeLight");

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
    void shouldDisplayAddedProductInCart() {
        inventoryPage.cartBadge().shouldNotHaveCartBadge();

        inventoryPage.addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name());

        CartPage cartPage = inventoryPage.openCart()
                .shouldBeOpened()
                .shouldHaveItemsCount(1)
                .shouldContainItem(BACKPACK.name())
                .shouldContainItemPrice(BACKPACK.name(), BACKPACK.price())
                .shouldHaveItemQuantity(BACKPACK.name(), 1);

        cartPage.cartBadge().shouldHaveCartBadge(1);
    }

    @Test
    void shouldRemoveProductFromCart() {
        inventoryPage.addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name());

        CartPage cartPage = inventoryPage.openCart()
                .shouldBeOpened()
                .shouldHaveItemsCount(1);

        cartPage.removeItem(BACKPACK.name())
                .shouldBeEmpty()
                .shouldNotContainItem(BACKPACK.name())
                .cartBadge()
                .shouldNotHaveCartBadge();
    }

    @Test
    void shouldReturnToInventoryFromCart() {
        inventoryPage.addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name());

        inventoryPage.openCart()
                .shouldBeOpened()
                .continueShopping()
                .shouldBeOpened()
                .shouldHaveItems(TestData.catalogCount());
    }

    @Test
    void shouldRemoveOneProductFromMultipleInCart() {
        inventoryPage.addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name())
                .addProductToCart(BIKE_LIGHT.name())
                .shouldHaveRemoveButton(BIKE_LIGHT.name());
        inventoryPage.cartBadge().shouldHaveCartBadge(2);

        inventoryPage.openCart()
                .shouldBeOpened()
                .shouldHaveItemsCount(2)
                .removeItem(BACKPACK.name())
                .shouldHaveItemsCount(1)
                .shouldNotContainItem(BACKPACK.name())
                .shouldContainItem(BIKE_LIGHT.name())
                .cartBadge()
                .shouldHaveCartBadge(1);
    }
}

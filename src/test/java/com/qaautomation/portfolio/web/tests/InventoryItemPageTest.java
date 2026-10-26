package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.InventoryItemPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("Product details")
class InventoryItemPageTest extends BaseWebTest {

    private static final TestData.Product BACKPACK = TestData.product("backpack");

    private final LoginPage loginPage = new LoginPage();
    private InventoryItemPage inventoryItemPage;

    @BeforeEach
    void loginAsStandardUser() {
        TestData.User user = TestData.webUser("standard");
        inventoryItemPage = loginPage.openLoginPage()
                .loginAs(user.username(), user.password())
                .shouldBeOpened()
                .openInventoryItem(BACKPACK.name())
                .shouldBeOpened()
                .shouldContainCorrectItemName(BACKPACK.name());
    }

    @Test
    void shouldAddProductToCartFromDetailsPage() {
        inventoryItemPage.cartBadge().shouldNotHaveCartBadge();
        inventoryItemPage.shouldHaveAddToCartButton()
                .addProductToCart()
                .shouldHaveRemoveButton();
        inventoryItemPage.cartBadge().shouldHaveCartBadge(1);

        inventoryItemPage.openCart()
                .shouldBeOpened()
                .shouldContainItem(BACKPACK.name())
                .shouldContainItemPrice(BACKPACK.name(), BACKPACK.price())
                .shouldHaveItemQuantity(BACKPACK.name(), 1);
    }

    @Test
    void shouldRemoveProductFromDetailsPage() {
        inventoryItemPage.cartBadge().shouldNotHaveCartBadge();
        inventoryItemPage.shouldHaveAddToCartButton()
                .addProductToCart()
                .shouldHaveRemoveButton();
        inventoryItemPage.cartBadge().shouldHaveCartBadge(1);

        inventoryItemPage.removeProductFromCart()
                .shouldHaveAddToCartButton();
        inventoryItemPage.cartBadge().shouldNotHaveCartBadge();

        inventoryItemPage.openCart()
                .shouldBeOpened()
                .shouldBeEmpty();
    }
}

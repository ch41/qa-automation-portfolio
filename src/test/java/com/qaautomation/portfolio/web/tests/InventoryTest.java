package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.RetryOnFailure;
import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.InventoryPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import com.qaautomation.portfolio.web.pages.SortOptions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("web")
@Epic("Web")
@Feature("Inventory")
class InventoryTest extends BaseWebTest {

    private static final TestData.Product BACKPACK = TestData.product("backpack");
    private static final TestData.Product BOLT_TSHIRT = TestData.product("boltTShirt");
    private static final TestData.Product RED_TSHIRT = TestData.product("redTShirt");

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
    void shouldDisplaySixProductsAfterLogin() {
        inventoryPage.shouldHaveItems(TestData.catalogCount());
    }

    @Test
    void shouldSortAlphabeticallyASCCorrectly() {
        inventoryPage.sortBy(SortOptions.NAME_DESC)
                .sortBy(SortOptions.NAME_ASC)
                .shouldHaveProductNamesInOrder(TestData.productNames("nameAsc"));
    }

    @Test
    void shouldSortAlphabeticallyDESCCorrectly() {
        inventoryPage.sortBy(SortOptions.NAME_DESC)
                .shouldHaveProductNamesInOrder(TestData.productNames("nameDesc"));
    }

    @Test
    void shouldSortPriceASCCorrectly() {
        inventoryPage.sortBy(SortOptions.PRICE_ASC)
                .shouldHaveProductPricesInOrder(TestData.productPrices("priceAsc"));
    }

    @Test
    void shouldSortPriceDESCCorrectly() {
        inventoryPage.sortBy(SortOptions.PRICE_DESC)
                .shouldHaveProductPricesInOrder(TestData.productPrices("priceDesc"));
    }

    @Test
    void shouldDisplayAllProductFields() {
        inventoryPage.shouldHaveAllFields();
    }

    @RetryOnFailure
    void shouldOpenAndCloseBurgerMenu() {
        inventoryPage.openBurgerMenu()
                .shouldBeOpened()
                .close()
                .shouldBeClosed();
    }

    @Tag("smoke")
    @Test
    void shouldLogoutUserFromBurgerMenu() {
        LoginPage loginPage = inventoryPage.openBurgerMenu()
                .logout();

        loginPage.shouldBeOpenedWithEmptyValues();
    }

    @Test
    void shouldContainAllBurgerMenuFields() {
        inventoryPage.openBurgerMenu()
                .shouldContainAllFields();
    }

    @Test
    void shouldReturnToInventoryFromAllItemsMenu() {
        inventoryPage.openInventoryItem(BACKPACK.name())
                .shouldBeOpened()
                .openBurgerMenu()
                .allItems()
                .shouldBeOpened();
    }

    @Test
    void shouldResetAppStateFromBurgerMenu() {
        inventoryPage.addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name());
        inventoryPage.cartBadge().shouldHaveCartBadge(1);

        inventoryPage.openBurgerMenu()
                .resetAppState()
                .close();

        inventoryPage.cartBadge().shouldNotHaveCartBadge();
        inventoryPage.openCart()
                .shouldBeOpened()
                .shouldBeEmpty()
                .continueShopping()
                .shouldBeOpened()
                .shouldHaveAddToCartButton(BACKPACK.name());
    }

    @Test
    void shouldAddProductToCart() {
        inventoryPage.cartBadge().shouldNotHaveCartBadge();

        inventoryPage.addProductToCart(BOLT_TSHIRT.name())
                .shouldHaveRemoveButton(BOLT_TSHIRT.name())
                .cartBadge()
                .shouldHaveCartBadge(1);
    }

    @Test
    void shouldAddMultipleProductsToCart() {
        inventoryPage.cartBadge().shouldNotHaveCartBadge();

        inventoryPage.addProductToCart(BOLT_TSHIRT.name())
                .shouldHaveRemoveButton(BOLT_TSHIRT.name())
                .addProductToCart(BACKPACK.name())
                .shouldHaveRemoveButton(BACKPACK.name())
                .addProductToCart(RED_TSHIRT.name())
                .shouldHaveRemoveButton(RED_TSHIRT.name())
                .cartBadge()
                .shouldHaveCartBadge(3);
    }

    @Test
    void shouldAddAllProductsToCart() {
        inventoryPage.cartBadge().shouldNotHaveCartBadge();

        inventoryPage.addAllProductsToCart()
                .shouldHaveRemoveButtonOnAllItems()
                .cartBadge()
                .shouldHaveCartBadge(TestData.catalogCount());
    }

    @Test
    void shouldOpenInventoryItem() {
        inventoryPage.openInventoryItem(BACKPACK.name())
                .shouldBeOpened()
                .shouldContainCorrectItemName(BACKPACK.name())
                .shouldContainCorrectItemPrice(BACKPACK.price())
                .shouldShowDesc()
                .clickOnBackToProducts()
                .shouldBeOpened();
    }

}

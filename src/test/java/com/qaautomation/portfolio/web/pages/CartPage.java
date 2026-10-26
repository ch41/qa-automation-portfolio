package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class CartPage extends AuthenticatedPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement continueShoppingButton = $("[data-test='continue-shopping']");
    private final SelenideElement checkoutButton = $("[data-test='checkout']");
    private final ElementsCollection cartItems = $$(".cart_item");

    public CartPage shouldBeOpened() {
        assertThat(url()).contains("/cart.html");
        title.shouldHave(exactText("Your Cart"));
        continueShoppingButton.shouldBe(visible);
        return this;
    }

    public CartPage shouldHaveItemsCount(int expectedCount) {
        cartItems.shouldHave(size(expectedCount));
        return this;
    }

    public CartPage shouldContainItem(String productName) {
        cartItem(productName)
                .$("[data-test='inventory-item-name']")
                .shouldHave(exactText(productName));
        return this;
    }

    public CartPage shouldContainItemPrice(String productName, String expectedPrice) {
        cartItem(productName)
                .$("[data-test='inventory-item-price']")
                .shouldHave(exactText(expectedPrice));
        return this;
    }

    public CartPage shouldHaveItemQuantity(String productName, int expectedQuantity) {
        cartItem(productName)
                .$("[data-test='item-quantity']")
                .shouldHave(exactText(String.valueOf(expectedQuantity)));
        return this;
    }

    public CartPage shouldNotContainItem(String productName) {
        $$("[data-test='inventory-item-name']")
                .findBy(exactText(productName))
                .shouldNotBe(visible);
        return this;
    }

    public CartPage shouldBeEmpty() {
        cartItems.shouldHave(size(0));
        return this;
    }

    public CartPage removeItem(String productName) {
        cartItem(productName).$("button")
                .shouldHave(exactText("Remove"))
                .click();
        return this;
    }

    public InventoryPage continueShopping() {
        continueShoppingButton.click();
        return new InventoryPage();
    }

    public CheckoutStepOnePage checkout() {
        checkoutButton.click();
        return new CheckoutStepOnePage();
    }

    private SelenideElement cartItem(String productName) {
        return $$("[data-test='inventory-item-name']")
                .findBy(exactText(productName))
                .closest(".cart_item");
    }
}

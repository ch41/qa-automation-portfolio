package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepTwoPage extends AuthenticatedPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement finishButton = $("[data-test='finish']");
    private final SelenideElement cancelButton = $("[data-test='cancel']");
    private final SelenideElement paymentInfo = $("[data-test='payment-info-value']");
    private final SelenideElement shippingInfo = $("[data-test='shipping-info-value']");
    private final SelenideElement subtotalLabel = $("[data-test='subtotal-label']");
    private final SelenideElement taxLabel = $("[data-test='tax-label']");
    private final SelenideElement totalLabel = $("[data-test='total-label']");
    private final ElementsCollection cartItems = $$(".cart_item");

    public CheckoutStepTwoPage shouldBeOpened() {
        assertThat(url()).contains("/checkout-step-two.html");
        title.shouldHave(exactText("Checkout: Overview"));
        cartItems.shouldHave(sizeGreaterThan(0));
        finishButton.shouldBe(visible);
        return this;
    }

    public CheckoutStepTwoPage shouldContainItem(String productName) {
        cartItem(productName)
                .$("[data-test='inventory-item-name']")
                .shouldHave(exactText(productName));
        return this;
    }

    public CheckoutStepTwoPage shouldContainItemPrice(String productName, String expectedPrice) {
        cartItem(productName)
                .$("[data-test='inventory-item-price']")
                .shouldHave(exactText(expectedPrice));
        return this;
    }

    public CheckoutStepTwoPage shouldHaveItemQuantity(String productName, int expectedQuantity) {
        cartItem(productName)
                .$("[data-test='item-quantity']")
                .shouldHave(exactText(String.valueOf(expectedQuantity)));
        return this;
    }

    public CheckoutStepTwoPage shouldHaveOrderTotals(String subtotal, String tax, String total) {
        subtotalLabel.shouldHave(exactText("Item total: " + subtotal));
        taxLabel.shouldHave(exactText("Tax: " + tax));
        totalLabel.shouldHave(exactText("Total: " + total));
        return this;
    }

    public CheckoutStepTwoPage shouldHavePaymentInfo(String expectedPaymentInfo) {
        paymentInfo.shouldHave(exactText(expectedPaymentInfo));
        return this;
    }

    public CheckoutStepTwoPage shouldHaveShippingInfo(String expectedShippingInfo) {
        shippingInfo.shouldHave(exactText(expectedShippingInfo));
        return this;
    }

    public CheckoutCompletePage finish() {
        finishButton.click();
        return new CheckoutCompletePage();
    }

    public InventoryPage cancel() {
        cancelButton.click();
        return new InventoryPage();
    }

    private SelenideElement cartItem(String productName) {
        return $$("[data-test='inventory-item-name']")
                .findBy(exactText(productName))
                .closest(".cart_item");
    }
}

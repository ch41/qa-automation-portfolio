package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutCompletePage extends AuthenticatedPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement completeHeader = $("[data-test='complete-header']");
    private final SelenideElement completeText = $("[data-test='complete-text']");
    private final SelenideElement backHomeButton = $("[data-test='back-to-products']");

    public CheckoutCompletePage shouldBeOpened() {
        assertThat(url()).contains("/checkout-complete.html");
        title.shouldHave(exactText("Checkout: Complete!"));
        backHomeButton.shouldBe(visible);
        return this;
    }

    public CheckoutCompletePage shouldShowConfirmation() {
        completeHeader.shouldHave(exactText("Thank you for your order!"));
        completeText.shouldHave(matchText(".*\\S.*"));
        return this;
    }

    public InventoryPage backHome() {
        backHomeButton.click();
        return new InventoryPage();
    }
}

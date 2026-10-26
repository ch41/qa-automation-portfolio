package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartBadge {

    private final SelenideElement shoppingCartBadge = $("[data-test='shopping-cart-badge']");

    public CartBadge shouldHaveCartBadge(int expectedCount) {
        shoppingCartBadge.shouldHave(exactText(String.valueOf(expectedCount)));
        return this;
    }

    public CartBadge shouldNotHaveCartBadge() {
        shoppingCartBadge.shouldNotBe(visible);
        return this;
    }
}

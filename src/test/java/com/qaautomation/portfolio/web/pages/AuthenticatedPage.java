package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class AuthenticatedPage extends BasePage {

    private final SelenideElement cartLink = $("[data-test='shopping-cart-link']");

    public BurgerMenu openBurgerMenu() {
        return new BurgerMenu().open();
    }

    public CartBadge cartBadge() {
        return new CartBadge();
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage();
    }
}

package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BurgerMenu {

    private final SelenideElement openButton = $("#react-burger-menu-btn");
    private final SelenideElement closeButton = $("#react-burger-cross-btn");
    private final SelenideElement menuContainer = $(".bm-menu-wrap");
    private final SelenideElement menuList = $(".bm-item-list");
    private final SelenideElement allItemsLink = $("[data-test='inventory-sidebar-link']");
    private final SelenideElement resetAppStateLink = $("[data-test='reset-sidebar-link']");
    private final ElementsCollection menuItems = $$(".bm-item-list > .bm-item");

    public BurgerMenu open() {
        openButton.shouldBe(visible).click();
        return shouldBeOpened();
    }

    public BurgerMenu close() {
        closeButton.shouldBe(visible).click();
        return shouldBeClosed();
    }

    public BurgerMenu shouldContainAllFields() {
        shouldBeOpened();
        closeButton.shouldBe(visible);
        menuItems.shouldHave(exactTexts(
                "All Items",
                "Dynamic Catalog",
                "About",
                "Logout",
                "Reset App State"
        ));
        menuItems.forEach(item -> item.shouldBe(visible));
        return this;
    }

    public BurgerMenu shouldBeOpened() {
        menuContainer.shouldBe(visible);
        menuList.shouldBe(visible);
        return this;
    }

    public BurgerMenu shouldBeClosed() {
        menuContainer.shouldNotBe(visible);
        menuList.shouldNotBe(visible);
        return this;
    }

    public LoginPage logout() {
        $("[data-test='logout-sidebar-link']").shouldBe(visible).click();
        return new LoginPage();
    }

    public InventoryPage allItems() {
        allItemsLink.shouldBe(visible).click();
        return new InventoryPage();
    }

    public BurgerMenu resetAppState() {
        resetAppStateLink.shouldBe(visible).click();
        return this;
    }
}

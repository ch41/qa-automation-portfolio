package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage extends AuthenticatedPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement inventoryList = $(".inventory_list");
    private final SelenideElement productsSortContainer = $(".product_sort_container");


    public InventoryPage shouldBeOpened() {
        title.shouldHave(text("Products"));
        inventoryList.shouldBe(visible);
        $$(".inventory_item").shouldHave(sizeGreaterThan(0));
        return this;
    }

    public InventoryPage shouldHaveItems(int expectedCount) {
        $$(".inventory_item").shouldHave(size(expectedCount));
        return this;
    }

    public InventoryPage shouldHaveSelectedValue(String expectedValue) {
        productsSortContainer.shouldHave(value(expectedValue));
        return this;
    }

    public InventoryPage shouldHaveProductNamesInOrder(String... expectedNames) {
        $$(".inventory_item_name").shouldHave(exactTexts(expectedNames));
        return this;
    }

    public InventoryPage shouldHaveProductPricesInOrder(String... expectedPrices) {
        $$(".inventory_item_price").shouldHave(exactTexts(expectedPrices));
        return this;
    }

    public InventoryPage sortBy(SortOptions sortOptions) {
        productsSortContainer.selectOptionByValue(sortOptions.value());
        shouldHaveSelectedValue(sortOptions.value());
        return this;
    }

    private SelenideElement productCard(String productName) {
        return $$("[data-test='inventory-item-name']")
                .findBy(text(productName))
                .closest(".inventory_item");
    }

    public InventoryPage shouldHaveAddToCartButton(String productName) {
        productCard(productName).$("button")
                .shouldHave(text("Add to cart"));
        return this;
    }

    public InventoryPage addProductToCart(String productName) {
        productCard(productName).$("button")
                .shouldHave(text("Add to cart"))
                .click();
        return this;
    }

    public InventoryPage addAllProductsToCart() {
        $$(".inventory_item").forEach(card ->
                card.$("button")
                        .shouldHave(text("Add to cart"))
                        .click()
        );
        return this;
    }

    public InventoryPage shouldHaveRemoveButtonOnAllItems() {
        $$(".inventory_item").forEach(card ->
                card.$("button").shouldHave(text("Remove"))
        );
        return this;
    }

    public InventoryPage shouldHaveRemoveButton(String productName) {
        productCard(productName).$("button")
                .shouldHave(text("Remove"));
        return this;
    }

    public InventoryPage shouldHaveAllFields() {
        $$(".inventory_item").forEach(card -> {
            card.$("img[src][alt]").shouldBe(visible);
            card.$(".inventory_item_name").shouldHave(matchText(".*\\S.*"));
            card.$(".inventory_item_desc").shouldHave(matchText(".*\\S.*"));
            card.$(".inventory_item_price").shouldHave(matchText("\\$\\d+\\.\\d{2}"));
            card.$("button").shouldBe(visible).shouldHave(matchText(".*\\S.*"));
        });
        return this;
    }

    public InventoryItemPage openInventoryItem(String itemName) {
        productCard(itemName).$(".inventory_item_name").click();
        return new InventoryItemPage();
    }
}


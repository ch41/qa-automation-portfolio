package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class InventoryItemPage extends AuthenticatedPage {

    private final SelenideElement backToProducts = $("[data-test='back-to-products']");
    private final SelenideElement inventoryItemPrice = $("[data-test='inventory-item-price']");
    private final SelenideElement inventoryItemName = $("[data-test='inventory-item-name']");
    private final SelenideElement inventoryItemDesc = $("[data-test='inventory-item-desc']");
    private final SelenideElement addToCartButton = $("[data-test='add-to-cart']");
    private final SelenideElement removeButton = $("[data-test='remove']");


    public InventoryItemPage shouldBeOpened() {
        assertThat(url()).contains("/inventory-item.html");
        backToProducts.shouldBe(visible);
        return this;
    }

    public InventoryItemPage shouldContainCorrectItemName(String itemName) {
        inventoryItemName.shouldHave(exactText(itemName));
        return this;
    }

    public InventoryItemPage shouldContainCorrectItemPrice(String itemPrice) {
        inventoryItemPrice.shouldHave(exactText(itemPrice));
        return this;
    }

    public InventoryItemPage shouldShowDesc() {
        inventoryItemDesc.shouldHave(matchText(".*\\S.*"));
        return this;
    }

    public InventoryPage clickOnBackToProducts() {
        backToProducts.click();
        return new InventoryPage();
    }

    public InventoryItemPage shouldHaveAddToCartButton() {
        addToCartButton.shouldBe(visible);
        removeButton.shouldNotBe(visible);
        return this;
    }

    public InventoryItemPage shouldHaveRemoveButton() {
        addToCartButton.shouldNotBe(visible);
        removeButton.shouldBe(visible);
        return this;
    }

    public InventoryItemPage addProductToCart() {
        addToCartButton.shouldHave(exactText("Add to cart"))
                .click();
        return this;
    }

    public InventoryItemPage removeProductFromCart() {
        removeButton.shouldHave(exactText("Remove"))
                .click();
        return this;
    }

}

package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepOnePage extends AuthenticatedPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement firstNameInput = $("[data-test='firstName']");
    private final SelenideElement lastNameInput = $("[data-test='lastName']");
    private final SelenideElement postalCodeInput = $("[data-test='postalCode']");
    private final SelenideElement continueButton = $("[data-test='continue']");
    private final SelenideElement cancelButton = $("[data-test='cancel']");
    private final SelenideElement errorMessage = $("[data-test='error']");
    private final SelenideElement closeErrorButton = $("[data-test='error-button']");

    public CheckoutStepOnePage shouldBeOpened() {
        assertThat(url()).contains("/checkout-step-one.html");
        title.shouldHave(exactText("Checkout: Your Information"));
        firstNameInput.shouldBe(visible);
        continueButton.shouldBe(visible);
        return this;
    }

    public CheckoutStepOnePage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        firstNameInput.setValue(firstName);
        lastNameInput.setValue(lastName);
        postalCodeInput.setValue(postalCode);
        return this;
    }

    public CheckoutStepOnePage shouldHaveCustomerInfo(String firstName, String lastName, String postalCode) {
        shouldHaveValue(firstNameInput, firstName);
        shouldHaveValue(lastNameInput, lastName);
        shouldHaveValue(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutStepTwoPage continueToOverview() {
        continueButton.click();
        return new CheckoutStepTwoPage();
    }

    public CheckoutStepOnePage continueExpectingError() {
        continueButton.click();
        return this;
    }

    public CheckoutStepOnePage closeError() {
        closeErrorButton.click();
        return this;
    }

    public CheckoutStepOnePage shouldShowError(String expectedMessage) {
        errorMessage.shouldHave(exactText(expectedMessage));
        return this;
    }

    public CheckoutStepOnePage shouldNotShowError() {
        errorMessage.shouldNotBe(visible);
        return this;
    }

    public CartPage cancel() {
        cancelButton.click();
        return new CartPage();
    }

    private void shouldHaveValue(SelenideElement input, String expectedValue) {
        if (expectedValue.isEmpty()) {
            input.shouldBe(empty);
        } else {
            input.shouldHave(exactValue(expectedValue));
        }
    }
}

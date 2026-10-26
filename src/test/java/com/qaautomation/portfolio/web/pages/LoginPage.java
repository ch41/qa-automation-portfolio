package com.qaautomation.portfolio.web.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    private final SelenideElement usernameInput = $("#user-name");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login-button");
    private final SelenideElement errorMessage = $("[data-test='error']");
    private final SelenideElement errorButton = $("[data-test='error-button']");

    public LoginPage openLoginPage() {
        openPage("/");
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        fillCredentials(username, password);
        loginButton.click();
        return new InventoryPage();
    }

    public LoginPage loginExpectingError(String username, String password) {
        fillCredentials(username, password);
        loginButton.click();
        return this;
    }

    public SelenideElement errorMessage() {
        return errorMessage;
    }

    public LoginPage closeError() {
        errorButton.shouldBe(visible).click();
        return this;
    }

    public LoginPage shouldNotShowError() {
        errorMessage.shouldNotBe(visible);
        return this;
    }

    private void fillCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
    }

    public void shouldBeOpenedWithEmptyValues() {
        usernameInput.shouldBe(empty);
        passwordInput.shouldBe(empty);
        loginButton.shouldBe(visible);
    }
}

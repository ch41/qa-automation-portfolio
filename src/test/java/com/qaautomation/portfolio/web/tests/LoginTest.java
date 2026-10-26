package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;

@Tag("web")
@Epic("Web")
@Feature("Authentication")
class LoginTest extends BaseWebTest {

    private final LoginPage loginPage = new LoginPage();

    @Tag("smoke")
    @Test
    @Story("Valid credentials")
    @DisplayName("User can log in with valid credentials")
    void shouldLoginWithValidCredentials() {
        TestData.User user = TestData.webUser("standard");

        loginPage.openLoginPage()
                .loginAs(user.username(), user.password())
                .shouldBeOpened()
                .shouldHaveItems(TestData.catalogCount());
    }

    @Test
    @Story("Invalid credentials")
    @DisplayName("User sees an error for invalid credentials")
    void shouldShowErrorForInvalidCredentials() {
        loginPage.openLoginPage()
                .loginExpectingError("invalid_user", "invalid_password")
                .errorMessage()
                .shouldHave(text("Username and password do not match"));
    }

    @Test
    @Story("Locked user")
    @DisplayName("Locked user cannot log in")
    void shouldRejectLockedUser() {
        TestData.User user = TestData.webUser("locked");

        loginPage.openLoginPage()
                .loginExpectingError(user.username(), user.password())
                .errorMessage()
                .shouldHave(text("Sorry, this user has been locked out"));
    }

    @Test
    @Story("Required fields")
    @DisplayName("User sees an error when username and password are empty")
    void shouldRequireUsernameWhenFieldsAreEmpty() {
        loginPage.openLoginPage()
                .loginExpectingError("", "")
                .errorMessage()
                .shouldHave(text("Username is required"));
    }

    @Test
    @Story("Required fields")
    @DisplayName("User sees an error when password is empty")
    void shouldRequirePasswordWhenUsernameIsFilled() {
        TestData.User user = TestData.webUser("standard");

        loginPage.openLoginPage()
                .loginExpectingError(user.username(), "")
                .errorMessage()
                .shouldHave(text("Password is required"));
    }

    @Test
    @Story("Error handling")
    @DisplayName("User can close the login error")
    void shouldCloseLoginError() {
        loginPage.openLoginPage()
                .loginExpectingError("invalid_user", "invalid_password")
                .closeError()
                .shouldNotShowError();
    }
}

package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.InventoryPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Selenide.open;

@Epic("Web")
@Feature("Access control")
class AuthGuardTest extends BaseWebTest {

    private final LoginPage loginPage = new LoginPage();
    private InventoryPage inventoryPage;

    @BeforeEach
    void loginAsStandardUser() {
        TestData.User user = TestData.webUser("standard");

        inventoryPage = loginPage.openLoginPage()
                .loginAs(user.username(), user.password())
                .shouldBeOpened();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/inventory.html",
            "/cart.html",
            "/checkout-step-one.html",
            "/checkout-step-two.html",
            "/checkout-complete.html"
    })
    @Story("Protected pages after logout")
    void shouldRedirectProtectedPageToLoginAfterLogout(String protectedUrl) {
        LoginPage loginPageAfterLogout = inventoryPage.openBurgerMenu()
                .logout();

        open(protectedUrl);

        loginPageAfterLogout.shouldBeOpenedWithEmptyValues();
    }
}

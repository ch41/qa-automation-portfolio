package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.CheckoutStepOnePage;
import com.qaautomation.portfolio.web.pages.CheckoutStepTwoPage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("Checkout cancel")
class CheckoutCancelTest extends BaseWebTest {

    private static final TestData.Product BACKPACK = TestData.product("backpack");
    private static final TestData.Customer CUSTOMER = TestData.checkoutCustomer();

    private final LoginPage loginPage = new LoginPage();
    private CheckoutStepOnePage checkoutStepOnePage;

    @BeforeEach
    void openCheckoutStepOne() {
        TestData.User user = TestData.webUser("standard");

        checkoutStepOnePage = loginPage.openLoginPage()
                .loginAs(user.username(), user.password())
                .shouldBeOpened()
                .addProductToCart(BACKPACK.name())
                .openCart()
                .shouldBeOpened()
                .checkout()
                .shouldBeOpened();
    }

    @Test
    @Story("Cancel from customer information")
    void shouldCancelFromStepOneAndKeepCart() {
        checkoutStepOnePage.cancel()
                .shouldBeOpened()
                .shouldContainItem(BACKPACK.name())
                .cartBadge()
                .shouldHaveCartBadge(1);
    }

    @Test
    @Story("Cancel from order overview")
    void shouldCancelFromStepTwoAndKeepCart() {
        CheckoutStepTwoPage checkoutStepTwoPage = checkoutStepOnePage
                .fillCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), CUSTOMER.postalCode())
                .continueToOverview()
                .shouldBeOpened();

        checkoutStepTwoPage.cancel()
                .shouldBeOpened()
                .shouldHaveRemoveButton(BACKPACK.name())
                .cartBadge()
                .shouldHaveCartBadge(1);
    }
}

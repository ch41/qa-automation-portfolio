package com.qaautomation.portfolio.web.tests;

import com.qaautomation.portfolio.core.web.BaseWebTest;
import com.qaautomation.portfolio.utils.TestData;
import com.qaautomation.portfolio.web.pages.CheckoutStepOnePage;
import com.qaautomation.portfolio.web.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("Checkout validation")
class CheckoutValidationTest extends BaseWebTest {

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
    @Story("Required fields")
    void shouldRequireFirstName() {
        checkoutStepOnePage.continueExpectingError()
                .shouldShowError("Error: First Name is required")
                .shouldBeOpened();
    }

    @Test
    @Story("Required fields")
    void shouldRequireLastName() {
        checkoutStepOnePage.fillCustomerInfo(CUSTOMER.firstName(), "", "")
                .continueExpectingError()
                .shouldShowError("Error: Last Name is required")
                .shouldHaveCustomerInfo(CUSTOMER.firstName(), "", "");
    }

    @Test
    @Story("Required fields")
    void shouldRequirePostalCode() {
        checkoutStepOnePage.fillCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), "")
                .continueExpectingError()
                .shouldShowError("Error: Postal Code is required")
                .shouldHaveCustomerInfo(CUSTOMER.firstName(), CUSTOMER.lastName(), "");
    }

    @Test
    @Story("Error handling")
    void shouldCloseValidationError() {
        checkoutStepOnePage.continueExpectingError()
                .shouldShowError("Error: First Name is required")
                .closeError()
                .shouldNotShowError();
    }
}

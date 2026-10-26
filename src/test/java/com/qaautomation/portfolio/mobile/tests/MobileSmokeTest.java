package com.qaautomation.portfolio.mobile.tests;

import com.qaautomation.portfolio.config.Config;
import com.qaautomation.portfolio.core.mobile.BaseMobileTest;
import com.qaautomation.portfolio.mobile.screens.ApiDemosScreen;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("mobile")
@Epic("Mobile")
@Feature("Smoke")
class MobileSmokeTest extends BaseMobileTest {

    @Test
    @Story("App launch")
    @DisplayName("Configured Android app starts successfully")
    void shouldLaunchConfiguredApp() {
        new ApiDemosScreen().shouldBeOpened();

        assertThat(driver.getCurrentPackage())
                .isEqualTo(Config.get("mobile.app.package"));
    }
}

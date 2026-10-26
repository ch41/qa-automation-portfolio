package com.qaautomation.portfolio.core.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Tag("mobile")
public abstract class BaseMobileTest {

    protected AndroidDriver driver;

    @BeforeEach
    void setUpMobileDriver() {
        driver = MobileDriverFactory.createDriver();
    }

    @AfterEach
    void tearDownMobileDriver() {
        MobileDriverFactory.quitDriver();
    }
}

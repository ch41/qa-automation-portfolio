package com.qaautomation.portfolio.mobile.screens;

import com.qaautomation.portfolio.config.Config;
import com.qaautomation.portfolio.core.mobile.MobileDriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseScreen {

    protected AndroidDriver driver() {
        return MobileDriverFactory.getDriver();
    }

    protected WebElement find(By locator) {
        return driver().findElement(locator);
    }

    protected WebElement waitForVisibility(By locator) {
        return new WebDriverWait(
                driver(),
                Duration.ofSeconds(Config.getLong("mobile.timeout"))
        ).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        waitForVisibility(locator).click();
    }
}

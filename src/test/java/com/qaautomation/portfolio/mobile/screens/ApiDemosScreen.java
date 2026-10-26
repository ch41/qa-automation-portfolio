package com.qaautomation.portfolio.mobile.screens;

import org.openqa.selenium.By;

public class ApiDemosScreen extends BaseScreen {

    private static final By TITLE = By.xpath("//*[@text='API Demos']");
    private static final By ACCESSIBILITY = By.xpath("//*[@text='Accessibility']");

    public ApiDemosScreen shouldBeOpened() {
        waitForVisibility(TITLE);
        return this;
    }

    public void openAccessibility() {
        tap(ACCESSIBILITY);
    }
}

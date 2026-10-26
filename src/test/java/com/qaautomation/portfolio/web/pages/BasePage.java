package com.qaautomation.portfolio.web.pages;

import static com.codeborne.selenide.Selenide.open;

public abstract class BasePage {

    protected void openPage(String relativeUrl) {
        open(relativeUrl);
    }
}

package com.qaautomation.portfolio.core.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.qaautomation.portfolio.config.Config;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Tag("web")
public abstract class BaseWebTest {

    private static final Path ALLURE_RESULTS = Path.of(
            System.getProperty("allure.results.directory", "build/allure-results")
    );

    static {
        SelenideLogger.addListener(
                "allure",
                new AllureSelenide().screenshots(true).savePageSource(true)
        );
    }

    @BeforeEach
    void setUpWebDriver() {
        Configuration.baseUrl = Config.get("web.base.url");
        Configuration.browser = Config.get("web.browser");
        Configuration.browserSize = Config.get("web.browser.size");
        Configuration.headless = Config.getBoolean("web.headless");
        Configuration.timeout = Config.getLong("web.timeout");

        writeAllureEnvironment();
    }

    private void writeAllureEnvironment() {
        String environment = """
                browser=%s
                browser.size=%s
                headless=%s
                base.url=%s
                """.formatted(
                Configuration.browser,
                Configuration.browserSize,
                Configuration.headless,
                Configuration.baseUrl
        );

        try {
            Files.createDirectories(ALLURE_RESULTS);
            Files.writeString(ALLURE_RESULTS.resolve("environment.properties"), environment);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to write Allure environment", exception);
        }
    }

    @AfterEach
    void tearDownWebDriver() {
        Selenide.closeWebDriver();
    }
}

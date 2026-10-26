package com.qaautomation.portfolio.core.mobile;

import com.qaautomation.portfolio.config.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class MobileDriverFactory {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private MobileDriverFactory() {
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Mobile driver has not been initialized");
        }
        return driver;
    }

    public static AndroidDriver createDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(Config.get("mobile.platform"))
                .setDeviceName(Config.get("mobile.device.name"))
                .setNoReset(Config.getBoolean("mobile.no.reset"));

        String appPath = Config.get("mobile.app.path", "");
        if (appPath.isBlank()) {
            options.setAppPackage(Config.get("mobile.app.package"));
            options.setAppActivity(Config.get("mobile.app.activity"));
        } else {
            options.setApp(appPath);
        }

        try {
            AndroidDriver driver = new AndroidDriver(
                    new URL(Config.get("mobile.appium.server.url")),
                    options
            );
            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(Config.getLong("mobile.timeout"))
            );
            DRIVER.set(driver);
            return driver;
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Invalid Appium server URL", exception);
        }
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            DRIVER.remove();
        }
    }
}

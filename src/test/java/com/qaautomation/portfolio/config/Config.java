package com.qaautomation.portfolio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final String BASE_CONFIG = "config/config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private Config() {
    }

    public static String get(String key) {
        return System.getProperty(
                key,
                System.getenv().getOrDefault(toEnvironmentVariable(key), PROPERTIES.getProperty(key))
        );
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static long getLong(String key) {
        return Long.parseLong(get(key));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        loadInto(properties, BASE_CONFIG);

        String environment = System.getProperty(
                "env",
                System.getenv().getOrDefault("ENV", properties.getProperty("env", "local"))
        );
        loadInto(properties, "config/" + environment + ".properties");
        return properties;
    }

    private static void loadInto(Properties properties, String resource) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration: " + resource, exception);
        }
    }

    private static String toEnvironmentVariable(String key) {
        return key.toUpperCase().replace('.', '_').replace('-', '_');
    }
}

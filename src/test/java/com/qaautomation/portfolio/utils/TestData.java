package com.qaautomation.portfolio.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public final class TestData {

    private static final JsonNode USERS = JsonUtils.readTree("/testdata/users.json");
    private static final JsonNode PRODUCTS = JsonUtils.readTree("/testdata/products.json");
    private static final JsonNode CHECKOUT = JsonUtils.readTree("/testdata/checkout.json");

    private TestData() {
    }

    public static User webUser(String name) {
        JsonNode user = USERS.path("web").path(name);
        if (user.isMissingNode()) {
            throw new IllegalArgumentException("Unknown web user: " + name);
        }
        return new User(user.path("username").asText(), user.path("password").asText());
    }

    public static Product product(String key) {
        JsonNode product = PRODUCTS.path("products").path(key);
        if (product.isMissingNode()) {
            throw new IllegalArgumentException("Unknown product: " + key);
        }
        return new Product(product.path("name").asText(), product.path("price").asText());
    }

    public static int catalogCount() {
        return PRODUCTS.path("catalog").path("count").asInt();
    }

    public static String[] productNames(String order) {
        return catalogProducts(order).stream()
                .map(Product::name)
                .toArray(String[]::new);
    }

    public static String[] productPrices(String order) {
        return catalogProducts(order).stream()
                .map(Product::price)
                .toArray(String[]::new);
    }

    public static Customer checkoutCustomer() {
        JsonNode customer = CHECKOUT.path("customer");
        return new Customer(
                customer.path("firstName").asText(),
                customer.path("lastName").asText(),
                customer.path("postalCode").asText()
        );
    }

    public static String checkoutPaymentInfo() {
        return CHECKOUT.path("paymentInfo").asText();
    }

    public static String checkoutShippingInfo() {
        return CHECKOUT.path("shippingInfo").asText();
    }

    public static Order checkoutOrder(String key) {
        JsonNode order = CHECKOUT.path("orders").path(key);
        if (order.isMissingNode()) {
            throw new IllegalArgumentException("Unknown checkout order: " + key);
        }

        List<Product> items = new ArrayList<>();
        order.path("items").forEach(item -> items.add(product(item.asText())));
        return new Order(
                List.copyOf(items),
                order.path("subtotal").asText(),
                order.path("tax").asText(),
                order.path("total").asText()
        );
    }

    private static List<Product> catalogProducts(String order) {
        JsonNode keys = PRODUCTS.path("catalog").path(order);
        if (!keys.isArray()) {
            throw new IllegalArgumentException("Unknown catalog order: " + order);
        }

        List<Product> products = new ArrayList<>();
        keys.forEach(key -> products.add(product(key.asText())));
        return products;
    }

    public record User(String username, String password) {
    }

    public record Product(String name, String price) {
    }

    public record Customer(String firstName, String lastName, String postalCode) {
    }

    public record Order(List<Product> items, String subtotal, String tax, String total) {
    }
}

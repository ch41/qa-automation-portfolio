package com.qaautomation.portfolio.api.spec;

import com.qaautomation.portfolio.config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecificationFactory {

    private RequestSpecificationFactory() {
    }

    public static RequestSpecification requestSpec() {
        return requestSpec(
                Config.get("api.base.url"),
                Config.get("api.base.path", "")
        );
    }

    public static RequestSpecification requestSpec(String baseUri) {
        return requestSpec(baseUri, "");
    }

    public static RequestSpecification requestSpec(String baseUri, String basePath) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
}

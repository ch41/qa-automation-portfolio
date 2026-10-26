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
        return new RequestSpecBuilder()
                .setBaseUri(Config.get("api.base.url"))
                .setBasePath(Config.get("api.base.path", ""))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
}

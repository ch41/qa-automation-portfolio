package com.qaautomation.portfolio.core.api;

import com.qaautomation.portfolio.api.spec.ResponseSpecificationFactory;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Tag;

@Tag("api")
public abstract class BaseApiTest {

    protected final ResponseSpecification responseSpec = ResponseSpecificationFactory.responseSpec();
}

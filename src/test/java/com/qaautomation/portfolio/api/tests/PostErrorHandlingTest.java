package com.qaautomation.portfolio.api.tests;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.qaautomation.portfolio.api.client.PostClient;
import com.qaautomation.portfolio.api.data.ApiTestData;
import com.qaautomation.portfolio.api.model.ApiError;
import com.qaautomation.portfolio.core.api.BaseApiTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("api")
@Epic("API")
@Feature("Error handling")
class PostErrorHandlingTest extends BaseApiTest {

    @RegisterExtension
    WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    @Story("Unauthorized")
    @DisplayName("401 is returned when credentials are rejected")
    void shouldReturnUnauthorized() {
        int postId = ApiTestData.postId();
        String error = ApiTestData.mockError("unauthorized");
        stubError(postId, 401, error, "Bearer");

        Response response = postClient().getPost(postId);

        response.then()
                .spec(responseSpec)
                .statusCode(401)
                .header("WWW-Authenticate", "Bearer")
                .body(matchesJsonSchemaInClasspath("schemas/api-error.json"))
                .body("error", equalTo(error));

        ApiError apiError = response.as(ApiError.class);
        assertThat(apiError.error()).isEqualTo(error);
        wireMock.verify(1, getRequestedFor(urlEqualTo("/posts/" + postId)));
    }

    @Test
    @Story("Not found")
    @DisplayName("404 is returned for a missing post")
    void shouldReturnNotFound() {
        int postId = ApiTestData.missingPostId();
        String error = ApiTestData.mockError("notFound");
        stubError(postId, 404, error);

        Response response = postClient().getPost(postId);

        response.then()
                .spec(responseSpec)
                .statusCode(404)
                .body(matchesJsonSchemaInClasspath("schemas/api-error.json"))
                .body("error", equalTo(error));

        ApiError apiError = response.as(ApiError.class);
        assertThat(apiError.error()).isEqualTo(error);
    }

    @Test
    @Story("Server error")
    @DisplayName("500 is returned when the server fails")
    void shouldReturnServerError() {
        int postId = ApiTestData.postId();
        String error = ApiTestData.mockError("serverError");
        stubError(postId, 500, error);

        Response response = postClient().getPost(postId);

        response.then()
                .spec(responseSpec)
                .statusCode(500)
                .body(matchesJsonSchemaInClasspath("schemas/api-error.json"))
                .body("error", equalTo(error));

        ApiError apiError = response.as(ApiError.class);
        assertThat(apiError.error()).isEqualTo(error);
    }

    @Test
    @Story("Malformed response")
    @DisplayName("Malformed JSON is preserved for diagnostics")
    void shouldPreserveMalformedJson() {
        int postId = ApiTestData.postId();
        String malformedJson = ApiTestData.malformedJson();
        wireMock.stubFor(get(urlEqualTo("/posts/" + postId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(malformedJson)));

        Response response = postClient().getPost(postId);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        assertThat(response.asString()).isEqualTo(malformedJson);
    }

    private PostClient postClient() {
        return new PostClient(wireMock.baseUrl());
    }

    private void stubError(int postId, int status, String error) {
        stubError(postId, status, error, null);
    }

    private void stubError(int postId, int status, String error, String authenticateHeader) {
        var response = aResponse()
                .withStatus(status)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\":\"" + error + "\"}");

        if (authenticateHeader != null) {
            response.withHeader("WWW-Authenticate", authenticateHeader);
        }

        wireMock.stubFor(get(urlEqualTo("/posts/" + postId)).willReturn(response));
    }
}

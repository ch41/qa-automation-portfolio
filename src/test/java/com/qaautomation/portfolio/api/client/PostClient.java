package com.qaautomation.portfolio.api.client;

import com.qaautomation.portfolio.api.model.Post;
import com.qaautomation.portfolio.api.spec.RequestSpecificationFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PostClient {

    private final RequestSpecification requestSpec = RequestSpecificationFactory.requestSpec();

    public Response getPost(int id) {
        return given()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .get("/posts/{id}");
    }

    public Response createPost(Post post) {
        return given()
                .spec(requestSpec)
                .body(post)
                .when()
                .post("/posts");
    }
}

package com.qaautomation.portfolio.api.client;

import com.qaautomation.portfolio.api.model.Post;
import com.qaautomation.portfolio.api.spec.RequestSpecificationFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PostClient {

    private final RequestSpecification requestSpec;

    public PostClient() {
        this(RequestSpecificationFactory.requestSpec());
    }

    public PostClient(String baseUri) {
        this(RequestSpecificationFactory.requestSpec(baseUri));
    }

    private PostClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response getPosts() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/posts");
    }

    public Response getPostsByUser(int userId) {
        return given()
                .spec(requestSpec)
                .queryParam("userId", userId)
                .when()
                .get("/posts");
    }

    public Response getPostsByUser(int userId, int limit) {
        return given()
                .spec(requestSpec)
                .queryParam("userId", userId)
                .queryParam("_limit", limit)
                .when()
                .get("/posts");
    }

    public Response getPost(int id) {
        return given()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .get("/posts/{id}");
    }

    public Response getCommentsForPost(int postId) {
        return given()
                .spec(requestSpec)
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}/comments");
    }

    public Response createPost(Post post) {
        return given()
                .spec(requestSpec)
                .body(post)
                .when()
                .post("/posts");
    }

    public Response updatePost(int id, Post post) {
        return given()
                .spec(requestSpec)
                .pathParam("id", id)
                .body(post)
                .when()
                .put("/posts/{id}");
    }

    public Response deletePost(int id) {
        return given()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .delete("/posts/{id}");
    }
}

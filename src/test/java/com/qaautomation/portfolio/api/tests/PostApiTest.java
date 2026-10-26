package com.qaautomation.portfolio.api.tests;

import com.qaautomation.portfolio.api.client.PostClient;
import com.qaautomation.portfolio.api.model.Post;
import com.qaautomation.portfolio.core.api.BaseApiTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

@Tag("api")
@Epic("API")
@Feature("Posts")
class PostApiTest extends BaseApiTest {

    private final PostClient postClient = new PostClient();

    @Test
    @Story("Get post")
    @DisplayName("GET /posts/{id} returns an existing post")
    void shouldReturnPostById() {
        postClient.getPost(1)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", not(emptyOrNullString()));
    }

    @Test
    @Story("Create post")
    @DisplayName("POST /posts creates a new post")
    void shouldCreatePost() {
        Post request = new Post(1, 0, "QA automation portfolio", "Created by RestAssured");

        Response response = postClient.createPost(request);
        response.then()
                .spec(responseSpec)
                .statusCode(201)
                .body("title", equalTo(request.title()));

        Post created = response.as(Post.class);
        assertThat(created.id()).isPositive();
        assertThat(created.body()).isEqualTo(request.body());
    }
}

package com.qaautomation.portfolio.api.tests;

import com.qaautomation.portfolio.api.client.PostClient;
import com.qaautomation.portfolio.api.data.ApiTestData;
import com.qaautomation.portfolio.api.model.Comment;
import com.qaautomation.portfolio.api.model.Post;
import com.qaautomation.portfolio.core.api.BaseApiTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
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
    @Story("List posts")
    @DisplayName("GET /posts returns a non-empty list of posts")
    void shouldReturnPosts() {
        Response response = postClient.getPosts();

        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/posts.json"));

        List<Post> posts = response.jsonPath().getList("", Post.class);
        assertThat(posts).isNotEmpty();
    }

    @Test
    @Story("Get post")
    @DisplayName("GET /posts/{id} returns an existing post")
    void shouldReturnPostById() {
        Response response = postClient.getPost(ApiTestData.postId());

        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post.json"))
                .body("id", equalTo(ApiTestData.postId()))
                .body("title", not(emptyOrNullString()));

        Post post = response.as(Post.class);
        assertThat(post.id()).isEqualTo(ApiTestData.postId());
        assertThat(post.userId()).isPositive();
    }

    @Test
    @Story("Filter posts")
    @DisplayName("GET /posts?userId returns posts for the selected user")
    void shouldFilterPostsByUser() {
        Response response = postClient.getPostsByUser(ApiTestData.userId());

        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/posts.json"));

        List<Integer> userIds = response.jsonPath().getList("userId", Integer.class);
        assertThat(userIds)
                .isNotEmpty()
                .allMatch(userId -> userId == ApiTestData.userId());
    }

    @Test
    @Story("Limit posts")
    @DisplayName("GET /posts?_limit limits the number of posts")
    void shouldLimitPosts() {
        Response response = postClient.getPostsByUser(ApiTestData.userId(), ApiTestData.limit());

        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/posts.json"));

        List<Post> posts = response.jsonPath().getList("", Post.class);
        assertThat(posts).hasSize(ApiTestData.limit());
    }

    @Test
    @Story("Post comments")
    @DisplayName("GET /posts/{id}/comments returns comments for the post")
    void shouldReturnCommentsForPost() {
        Response response = postClient.getCommentsForPost(ApiTestData.commentsPostId());

        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/comments.json"));

        List<Comment> comments = response.jsonPath().getList("", Comment.class);
        assertThat(comments)
                .isNotEmpty()
                .allSatisfy(comment -> assertThat(comment.postId()).isEqualTo(ApiTestData.commentsPostId()));
    }

    @Test
    @Story("Create post")
    @DisplayName("POST /posts creates a new post")
    void shouldCreatePost() {
        Post request = ApiTestData.createPost();

        Response response = postClient.createPost(request);
        response.then()
                .spec(responseSpec)
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/post.json"))
                .body("title", equalTo(request.title()));

        Post created = response.as(Post.class);
        assertThat(created.id()).isPositive();
        assertThat(created.userId()).isEqualTo(request.userId());
        assertThat(created.title()).isEqualTo(request.title());
        assertThat(created.body()).isEqualTo(request.body());
    }

    @Test
    @Story("Update post")
    @DisplayName("PUT /posts/{id} updates an existing post")
    void shouldUpdatePost() {
        Post request = ApiTestData.updatePost();

        Response response = postClient.updatePost(ApiTestData.postId(), request);
        response.then()
                .spec(responseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post.json"))
                .body("title", equalTo(request.title()));

        Post updated = response.as(Post.class);
        assertThat(updated.id()).isEqualTo(ApiTestData.postId());
        assertThat(updated.userId()).isEqualTo(request.userId());
        assertThat(updated.title()).isEqualTo(request.title());
        assertThat(updated.body()).isEqualTo(request.body());
    }

    @Test
    @Story("Delete post")
    @DisplayName("DELETE /posts/{id} deletes an existing post")
    void shouldDeletePost() {
        Response response = postClient.deletePost(ApiTestData.postId());

        response.then()
                .spec(responseSpec)
                .statusCode(200);

        assertThat(response.jsonPath().getMap("$")).isEmpty();
    }
}

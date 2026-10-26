package com.qaautomation.portfolio.api.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.qaautomation.portfolio.api.model.Post;
import com.qaautomation.portfolio.utils.JsonUtils;

public final class ApiTestData {

    private static final JsonNode API = JsonUtils.readTree("/testdata/api.json");

    private ApiTestData() {
    }

    public static int postId() {
        return API.path("postId").asInt();
    }

    public static int missingPostId() {
        return API.path("missingPostId").asInt();
    }

    public static int userId() {
        return API.path("userId").asInt();
    }

    public static int limit() {
        return API.path("limit").asInt();
    }

    public static int commentsPostId() {
        return API.path("commentsPostId").asInt();
    }

    public static Post createPost() {
        return post("createPost");
    }

    public static Post updatePost() {
        return post("updatePost");
    }

    public static String mockError(String name) {
        JsonNode error = API.path("mock").path(name);
        if (error.isMissingNode()) {
            throw new IllegalArgumentException("Unknown mock error: " + name);
        }
        return error.asText();
    }

    public static String malformedJson() {
        return mockError("malformedJson");
    }

    private static Post post(String key) {
        JsonNode post = API.path(key);
        if (post.isMissingNode()) {
            throw new IllegalArgumentException("Unknown API post: " + key);
        }
        return new Post(
                post.path("userId").asInt(),
                post.path("id").asInt(),
                post.path("title").asText(),
                post.path("body").asText()
        );
    }
}

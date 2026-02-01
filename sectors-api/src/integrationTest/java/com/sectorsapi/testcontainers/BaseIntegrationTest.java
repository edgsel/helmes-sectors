package com.sectorsapi.testcontainers;

import com.helmes.sectorsapi.SectorsApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@AutoConfigureWebTestClient
@SpringBootTest(classes = SectorsApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    protected static final String USER_REGISTER_URI = "/api/v1/users/register";
    protected static final String USER_LOGIN_URI = "/api/v1/users/login";
    protected static final String SECTORS_TREE_URI = "/api/v1/sectors/tree";
    protected static final String APPLICATIONS_URI = "/api/v1/users/applications";

    @Autowired
    protected WebTestClient webTestClient;

    @Container
    static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("jwt.secret-key", () -> "dGVzdFNlY3JldEtleVRoYXRJc0F0TGVhc3QzMkJ5dGVzTG9uZw==");
        registry.add("jwt.expiration-ms", () -> "3600000");
    }
}
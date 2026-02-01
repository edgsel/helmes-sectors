package com.sectorsapi.controller;

import com.helmes.sectorsapi.dto.request.UserAuthDTO;
import com.helmes.sectorsapi.dto.response.AuthResponseDTO;
import com.helmes.sectorsapi.dto.response.ErrorResponseDTO;
import com.helmes.sectorsapi.dto.response.SectorResponseDTO;
import com.helmes.sectorsapi.repository.UserRepository;
import com.sectorsapi.testcontainers.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SectorControllerIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        var request = new UserAuthDTO("loginuser", "password123");
        var response = webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(AuthResponseDTO.class)
            .returnResult()
            .getResponseBody();

        jwtToken = response.jwt();
    }

    @Test
    void getSectorTree_Success_ReturnsTree() {
        // when
        var response = webTestClient.get()
            .uri(SECTORS_TREE_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(SectorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        var hasChildren = response.stream()
            .anyMatch(r -> r.children() != null && !r.children().isEmpty());

        // then
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertTrue(hasChildren);
    }

    @Test
    void getSectorTree_NoAuth_Returns401() {
        // when
        var response = webTestClient.get()
            .uri(SECTORS_TREE_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Unauthorized: Invalid or missing authentication token");
    }
}

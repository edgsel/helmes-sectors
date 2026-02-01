package com.sectorsapi.controller;

import com.helmes.sectorsapi.dto.request.UserAuthDTO;
import com.helmes.sectorsapi.dto.response.AuthResponseDTO;
import com.helmes.sectorsapi.dto.response.ErrorResponseDTO;
import com.helmes.sectorsapi.repository.UserRepository;
import com.sectorsapi.testcontainers.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static com.helmes.sectorsapi.exception.ErrorCode.USER_EXISTS_ERROR;
import static com.helmes.sectorsapi.exception.ErrorCode.VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_Success_Returns201WithJwt() {
        // given
        var request = new UserAuthDTO("testuser", "password123");

        // when
        var response = webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(AuthResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.jwt()).isNotNull();
        assertThat(response.jwt()).isNotBlank();
        assertTrue(userRepository.existsByUsername(request.username()));
    }

    @Test
    void register_DuplicateUsername_Returns409() {
        // given
        var request = new UserAuthDTO("duplicate", "password123");

        webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(AuthResponseDTO.class)
            .returnResult();

        // when
        var response = webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT,APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(USER_EXISTS_ERROR);
        assertThat(response.description()).containsIgnoringCase("Username duplicate already exists");
    }

    @Test
    void register_UsernameTooShort_Returns400() {
        // given
        var request = new UserAuthDTO("ab", "password123");

        // when
        var response = webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT,APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(VALIDATION_ERROR);
        assertThat(response.description()).containsIgnoringCase("Username must be between 3 and 50 characters");
    }

    @Test
    void register_PasswordTooShort_Returns400() {
        // given
        var request = new UserAuthDTO("validuser", "short");

        // when
        var response = webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT,APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(VALIDATION_ERROR);
        assertThat(response.description()).containsIgnoringCase("Password must be at least 8 characters");
    }

    @Test
    void login_Success_Returns200WithJwt() {
        // given
        var request = new UserAuthDTO("loginuser", "password123");
        webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(AuthResponseDTO.class)
            .returnResult();

        // when
        var response = webTestClient.post()
            .uri(USER_LOGIN_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(AuthResponseDTO.class)
            .returnResult()
            .getResponseBody();


        // then
        assertThat(response).isNotNull();
        assertThat(response.jwt()).isNotBlank();
    }

    @Test
    void login_WrongPassword_Returns401() {
        // given
        var registerRequest = new UserAuthDTO("wrongpass", "password123");
        var loginRequest = new UserAuthDTO("wrongpass", "wrongpassword");

        // when
        webTestClient.post()
            .uri(USER_REGISTER_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(registerRequest)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(AuthResponseDTO.class)
            .returnResult();

        var response = webTestClient.post()
            .uri(USER_LOGIN_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(loginRequest)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Invalid username or password");
    }

    @Test
    void login_UserNotFound_Returns401() {
        // given
        var loginRequest = new UserAuthDTO("nonexistent", "password123");

        // when
        var response = webTestClient.post()
            .uri(USER_LOGIN_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(loginRequest)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Invalid username or password");
    }
}

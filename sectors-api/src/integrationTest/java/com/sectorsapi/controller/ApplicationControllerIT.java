package com.sectorsapi.controller;

import com.helmes.sectorsapi.dto.request.CreateApplicationDTO;
import com.helmes.sectorsapi.dto.request.UpdateApplicationDTO;
import com.helmes.sectorsapi.dto.request.UserAuthDTO;
import com.helmes.sectorsapi.dto.response.ApplicationResponseDTO;
import com.helmes.sectorsapi.dto.response.AuthResponseDTO;
import com.helmes.sectorsapi.dto.response.ErrorResponseDTO;
import com.helmes.sectorsapi.repository.ApplicationRepository;
import com.helmes.sectorsapi.repository.UserRepository;
import com.sectorsapi.testcontainers.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

import static com.helmes.sectorsapi.exception.ErrorCode.APPLICATION_NOT_FOUND;
import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static com.helmes.sectorsapi.exception.ErrorCode.SECTORS_NOT_FOUND;
import static com.helmes.sectorsapi.exception.ErrorCode.VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ApplicationControllerIT extends BaseIntegrationTest {

    // taken from insert migration script
    private static final Long LEAF_SECTOR_ID = 79L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        applicationRepository.deleteAll();
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
    void createApplication_Success_Returns201() {
        // given
        var request = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), true);

        // when
        var response = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.applicationId()).isNotNull();
        assertThat(response.applicantName()).isEqualTo(request.applicantName());
        assertTrue(response.agreedToTerms());
    }

    @Test
    void createApplication_NoAuth_Returns401() {
        // given
        var request = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), true);

        // when
        var response = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Unauthorized: Invalid or missing authentication token");
    }

    @Test
    void createApplication_InvalidSectorId_Returns404() {
        // given
        var request = new CreateApplicationDTO("John Doe", Set.of(999999L), true);

        // when
        var response = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(SECTORS_NOT_FOUND);
        assertThat(response.description()).containsIgnoringCase("Sectors not found: [999999]");
    }

    @Test
    void createApplication_EmptyName_Returns400() {
        // given
        var request = new CreateApplicationDTO("", Set.of(LEAF_SECTOR_ID), true);

        // when
        var response = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(VALIDATION_ERROR);
        assertThat(response.description()).containsIgnoringCase("Name cannot be empty");
    }

    @Test
    void createApplication_NotAgreedToTerms_Returns400() {
        // given
        var request = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), false);

        // when
        var response = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(VALIDATION_ERROR);
        assertThat(response.description()).containsIgnoringCase("Terms must be accepted");
    }

    @Test
    void getAllApplications_Success_ReturnsList() {
        // given
        var request = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), true);

        webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // when
        var response = webTestClient.get()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(1);
    }

    @Test
    void getAllApplications_NoAuth_Returns401() {
        // when
        var response = webTestClient.get()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Unauthorized: Invalid or missing authentication token");
    }

    @Test
    void getAllApplications_Empty_ReturnsEmptyList() {
        // when
        var response = webTestClient.get()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    void getApplicationById_Success_ReturnsApplication() {
        // given
        var createRequest = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), true);
        var createResponse = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // when
        var getResponse = webTestClient.get()
            .uri(APPLICATIONS_URI + "/" + createResponse.applicationId())
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(getResponse).isNotNull();
        assertThat(getResponse.sectorIds()).isNotNull();
        assertThat(getResponse.sectorIds()).isNotEmpty();
        assertThat(getResponse.sectorIds()).hasSize(1);
        assertThat(getResponse.applicantName()).isEqualTo(createResponse.applicantName());
    }

    @Test
    void getApplicationById_NotFound_Returns404() {
        // given
        var randomId = UUID.randomUUID();

        // when
        var response = webTestClient.get()
            .uri(APPLICATIONS_URI + "/" + randomId)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        assertThat(response.code()).isEqualTo(APPLICATION_NOT_FOUND);
        assertThat(response.description()).containsIgnoringCase("Application %s not found or access denied".formatted(randomId));
    }

    @Test
    void updateApplication_Success_ReturnsUpdated() {
        // given
        var createRequest = new CreateApplicationDTO("John Doe", Set.of(LEAF_SECTOR_ID), true);
        var createResponse = webTestClient.post()
            .uri(APPLICATIONS_URI)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();
        var updateRequest = new UpdateApplicationDTO("Jane Doe", Set.of(LEAF_SECTOR_ID));

        // when
        var updateResponse = webTestClient.put()
            .uri(APPLICATIONS_URI + "/" + createResponse.applicationId())
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(updateRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ApplicationResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.sectorIds()).isNotNull();
        assertThat(updateResponse.sectorIds()).isNotEmpty();
        assertThat(updateResponse.applicantName()).isEqualTo(updateRequest.applicantName());
    }

    @Test
    void updateApplication_NotFound_Returns404() {
        // given
        var randomId = UUID.randomUUID();
        var request = new UpdateApplicationDTO("Jane Doe", Set.of(LEAF_SECTOR_ID));

        // when
        var response = webTestClient.put()
            .uri(APPLICATIONS_URI + "/" + randomId)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .bodyValue(request)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(APPLICATION_NOT_FOUND);
        assertThat(response.description()).containsIgnoringCase("Application %s not found or access denied".formatted(randomId));
    }

    @Test
    void updateApplication_NoAuth_Returns401() {
        // given
        var request = new UpdateApplicationDTO("Jane Doe", Set.of(LEAF_SECTOR_ID));

        // when
        var response = webTestClient.put()
            .uri(APPLICATIONS_URI + "/" + UUID.randomUUID())
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .bodyValue(request)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // then
        assertThat(response.code()).isEqualTo(INVALID_CREDENTIALS);
        assertThat(response.description()).containsIgnoringCase("Unauthorized: Invalid or missing authentication token");
    }
}

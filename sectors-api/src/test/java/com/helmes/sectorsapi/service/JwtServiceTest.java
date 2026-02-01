package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.config.properties.JwtProperties;
import com.helmes.sectorsapi.exception.InvalidJwtException;
import com.helmes.sectorsapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        var properties = new JwtProperties(
            "dGVzdC1zZWNyZXQta2V5LXRoYXQtaXMtYXQtbGVhc3QtMzItYnl0ZXM=", // base64 encoded
            3600000L
        );
        jwtService = new JwtService(properties);
    }

    @Test
    void generateToken_ReturnsValidToken() {
        // given
        var user = User.builder()
            .id(1L)
            .username("testuser")
            .build();

        // when
        var token = jwtService.generateToken(user);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtService.getUsername(token)).isEqualTo(user.getUsername());
    }

    @Test
    void getUsername_InvalidToken_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> jwtService.getUsername("invalid-token")).isInstanceOf(InvalidJwtException.class);
    }
}

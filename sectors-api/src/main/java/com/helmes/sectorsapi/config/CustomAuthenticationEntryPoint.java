package com.helmes.sectorsapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmes.sectorsapi.dto.response.ErrorResponseDTO;
import com.helmes.sectorsapi.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var errorDTO = ErrorResponseDTO.builder()
            .description("Unauthorized: Invalid or missing authentication token")
            .code(ErrorCode.INVALID_CREDENTIALS.name())
            .build();

        objectMapper.writeValue(response.getWriter(), errorDTO);
    }
}

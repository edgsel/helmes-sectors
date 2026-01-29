package com.helmes.sectorsapi.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponseDTO(String description, String code) {
}

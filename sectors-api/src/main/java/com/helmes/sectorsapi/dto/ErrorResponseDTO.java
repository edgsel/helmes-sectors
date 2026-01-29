package com.helmes.sectorsapi.dto;

import lombok.Builder;

@Builder
public record ErrorResponseDTO(String description, String code) {
}

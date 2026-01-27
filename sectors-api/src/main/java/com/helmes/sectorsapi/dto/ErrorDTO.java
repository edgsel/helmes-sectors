package com.helmes.sectorsapi.dto;

import lombok.Builder;

@Builder
public record ErrorDTO(String description, String code) {
}

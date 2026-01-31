package com.helmes.sectorsapi.dto.response;

import com.helmes.sectorsapi.exception.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorResponseDTO(String description, ErrorCode code) {
}

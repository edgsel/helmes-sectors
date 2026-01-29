package com.helmes.sectorsapi.dto.response;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ApplicationSummaryResponseDTO(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}

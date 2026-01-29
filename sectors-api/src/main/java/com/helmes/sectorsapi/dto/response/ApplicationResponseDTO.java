package com.helmes.sectorsapi.dto.response;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record ApplicationResponseDTO(
    UUID applicationId,
    String applicantName,
    Set<Long> sectorIds,
    Boolean agreedToTerms
) {}
